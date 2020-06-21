package net.sf.yad2xx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static java.lang.String.format;
import static net.sf.yad2xx.util.NativeLibraryLoader.OSArch.Arch.IA64;
import static net.sf.yad2xx.util.NativeLibraryLoader.OSArch.Arch.X86;
import static net.sf.yad2xx.util.NativeLibraryLoader.OSArch.Arch.X86_64;

public class NativeLibraryLoader {

    private static final Logger log = LoggerFactory.getLogger(NativeLibraryLoader.class);

    private static final String FTDI_LIBRARY_NAME = "libFTDIInterface";
    private static final String NATIVE_LIBRARY_PATH_PREFIX = "native";

    public static void loadLibrary() {

        try {
            String libraryPath = extractLibrary(FTDI_LIBRARY_NAME);
            System.load(libraryPath);
            log.debug("Successfully loaded library {}", FTDI_LIBRARY_NAME);
        } catch (IOException e) {
            log.warn(
                    "Could not find library {} as resource, trying fallback lookup through System.loadLibrary",
                    FTDI_LIBRARY_NAME
            );
            System.loadLibrary(resolveNativeLibraryName(FTDI_LIBRARY_NAME));
        }
    }

    private static String extractLibrary(String library) throws IOException {

        String resource = resolveLibraryPath(library);
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resource)) {
            File file = File.createTempFile(resolveNativeLibraryName(library), null);
            file.deleteOnExit();
            try (OutputStream os = new FileOutputStream(file)) {
                copy(is, os);
            }
            return file.getAbsolutePath();
        }
    }

    private static String resolveLibraryPath(String library) {

        OSArch osArch = osArch();
        return format(
                "%s/%s/%s",
                NATIVE_LIBRARY_PATH_PREFIX,
                osArch.name().toLowerCase(),
                resolveNativeLibraryName(library)
        );
    }

    private static String resolveNativeLibraryName(String library) {

        OSArch osArch = osArch();
        return format(
                "%s.%s",
                library,
                toExt(osArch)
        );
    }

    private static void copy(InputStream is, OutputStream os) throws IOException {

        int cnt;
        byte[] buf = new byte[16 * 1024];
        while ((cnt = is.read(buf)) >= 1) {
            os.write(buf, 0, cnt);
        }
    }

    private static String toExt(OSArch osArch) {

        switch (osArch) {
            case OSX_X86_64:
                return "jnilib";

            case WINDOWS_X86:
                return "dll";

            default:
                return "so";
        }
    }

    private static OSArch osArch() {

        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        for (OSArch v : OSArch.values()) {
            if (os.contains(v.getOS()) && arch.equalsIgnoreCase(v.getArch().getArch())) {
                return v;
            }
        }

        throw new IllegalStateException(format(
                "Failed to find matching OSType for %s:%s",
                os,
                arch
        ));
    }

    public enum OSArch {
        OSX_X86_64("os x", X86_64),
        WINDOWS_X86("win", X86),
        LINUX_X86_64("linux", X86_64),
        LINUX_IA64("linux", IA64),
        LINUX_I386("linux", X86);

        private final String os;
        private final Arch arch;

        OSArch(String os, Arch arch) {

            this.os = os;
            this.arch = arch;
        }

        public Arch getArch() {

            return arch;
        }

        public String getOS() {

            return os;
        }

        public enum Arch {
            X86_32("x86_32"),
            X86_64("x86_64"),
            X86("x86"),
            AMD64("amd64"),
            I386("i386"),
            IA64("ia64");

            private final String arch;

            Arch(String arch) {

                this.arch = arch;
            }

            public String getArch() {

                return arch;
            }
        }
    }
}