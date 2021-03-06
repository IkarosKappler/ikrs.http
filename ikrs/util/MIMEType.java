package ikrs.util;

import java.util.Map;
import java.util.TreeMap;

/**
 * This is a simple MIME type implementation.
 * The main purpose of this class is a mapping filename extension <-> MIME type.
 *
 *
 * @author Ikaros Kappler
 * @version 1.0.0
 * @date 2012-07-29
 **/

public class MIMEType {

    private static Map<String,String> extensionMap;
    
    static {

	extensionMap = new TreeMap<String,String>( CaseInsensitiveComparator.sharedInstance );
	extensionMap.put("3dm", "x-world/x-3dmf");
	extensionMap.put("3dmf", "x-world/x-3dmf");
	extensionMap.put("a", "application/octet-stream");
	extensionMap.put("aab", "application/x-authorware-bin");
	extensionMap.put("aam", "application/x-authorware-map");
	extensionMap.put("aas", "application/x-authorware-seg");
	extensionMap.put("abc", "text/vnd.abc");
	extensionMap.put("acgi", "text/html");
	extensionMap.put("afl", "video/animaflex");
	extensionMap.put("ai", "application/postscript");
	extensionMap.put("aif", "audio/aiff");
	extensionMap.put("aif", "audio/x-aiff");
	extensionMap.put("aifc", "audio/aiff");
	extensionMap.put("aifc", "audio/x-aiff");
	extensionMap.put("aiff", "audio/aiff");
	extensionMap.put("aiff", "audio/x-aiff");
	extensionMap.put("aim", "application/x-aim");
	extensionMap.put("aip", "text/x-audiosoft-intra");
	extensionMap.put("ani", "application/x-navi-animation");
	extensionMap.put("aos", "application/x-nokia-9000-communicator-add-on-software");
	extensionMap.put("aps", "application/mime");
	extensionMap.put("arc", "application/octet-stream");
	extensionMap.put("arj", "application/arj");
	extensionMap.put("arj", "application/octet-stream");
	extensionMap.put("art", "image/x-jg");
	extensionMap.put("asf", "video/x-ms-asf");
	extensionMap.put("asm", "text/x-asm");
	extensionMap.put("asp", "text/asp");
	extensionMap.put("asx", "application/x-mplayer2");
	extensionMap.put("asx", "video/x-ms-asf");
	extensionMap.put("asx", "video/x-ms-asf-plugin");
	extensionMap.put("au", "audio/basic");
	extensionMap.put("au", "audio/x-au");
	extensionMap.put("avi", "application/x-troff-msvideo");
	extensionMap.put("avi", "video/avi");
	extensionMap.put("avi", "video/msvideo");
	extensionMap.put("avi", "video/x-msvideo");
	extensionMap.put("avs", "video/avs-video");
	extensionMap.put("bcpio", "application/x-bcpio");
	extensionMap.put("bin", "application/mac-binary");
	extensionMap.put("bin", "application/macbinary");
	extensionMap.put("bin", "application/octet-stream");
	extensionMap.put("bin", "application/x-binary");
	extensionMap.put("bin", "application/x-macbinary");
	extensionMap.put("bm", "image/bmp");
	extensionMap.put("bmp", "image/bmp");
	extensionMap.put("bmp", "image/x-windows-bmp");
	extensionMap.put("boo", "application/book");
	extensionMap.put("book", "application/book");
	extensionMap.put("boz", "application/x-bzip2");
	extensionMap.put("bsh", "application/x-bsh");
	extensionMap.put("bz", "application/x-bzip");
	extensionMap.put("bz2", "application/x-bzip2");
	extensionMap.put("c", "text/plain");
	extensionMap.put("c", "text/x-c");
	extensionMap.put("c++", "text/plain");
	extensionMap.put("cat", "application/vnd.ms-pki.seccat");
	extensionMap.put("cc", "text/plain");
	extensionMap.put("cc", "text/x-c");
	extensionMap.put("ccad", "application/clariscad");
	extensionMap.put("cco", "application/x-cocoa");
	extensionMap.put("cdf", "application/cdf");
	extensionMap.put("cdf", "application/x-cdf");
	extensionMap.put("cdf", "application/x-netcdf");
	extensionMap.put("cer", "application/pkix-cert");
	extensionMap.put("cer", "application/x-x509-ca-cert");
	extensionMap.put("cha", "application/x-chat");
	extensionMap.put("chat", "application/x-chat");
	extensionMap.put("class", "application/java");
	extensionMap.put("class", "application/java-byte-code");
	extensionMap.put("class", "application/x-java-class");
	extensionMap.put("com", "application/octet-stream");
	extensionMap.put("com", "text/plain");
	extensionMap.put("conf", "text/plain");
	extensionMap.put("cpio", "application/x-cpio");
	extensionMap.put("cpp", "text/x-c");
	extensionMap.put("cpt", "application/mac-compactpro");
	extensionMap.put("cpt", "application/x-compactpro");
	extensionMap.put("cpt", "application/x-cpt");
	extensionMap.put("crl", "application/pkcs-crl");
	extensionMap.put("crl", "application/pkix-crl");
	extensionMap.put("crt", "application/pkix-cert");
	extensionMap.put("crt", "application/x-x509-ca-cert");
	extensionMap.put("crt", "application/x-x509-user-cert");
	extensionMap.put("csh", "application/x-csh");
	extensionMap.put("csh", "text/x-script.csh");
	extensionMap.put("css", "application/x-pointplus");
	extensionMap.put("css", "text/css");
	extensionMap.put("cxx", "text/plain");
	extensionMap.put("dcr", "application/x-director");
	extensionMap.put("deepv", "application/x-deepv");
	extensionMap.put("def", "text/plain");
	extensionMap.put("der", "application/x-x509-ca-cert");
	extensionMap.put("dif", "video/x-dv");
	extensionMap.put("dir", "application/x-director");
	extensionMap.put("dl", "video/dl");
	extensionMap.put("dl", "video/x-dl");
	extensionMap.put("doc", "application/msword");
	extensionMap.put("dot", "application/msword");
	extensionMap.put("dp", "application/commonground");
	extensionMap.put("drw", "application/drafting");
	extensionMap.put("dump", "application/octet-stream");
	extensionMap.put("dv", "video/x-dv");
	extensionMap.put("dvi", "application/x-dvi");
	extensionMap.put("dwf", "drawing/x-dwf (old)");
	extensionMap.put("dwf", "model/vnd.dwf");
	extensionMap.put("dwg", "application/acad");
	extensionMap.put("dwg", "image/vnd.dwg");
	extensionMap.put("dwg", "image/x-dwg");
	extensionMap.put("dxf", "application/dxf");
	extensionMap.put("dxf", "image/vnd.dwg");
	extensionMap.put("dxf", "image/x-dwg");
	extensionMap.put("dxr", "application/x-director");
	extensionMap.put("el", "text/x-script.elisp");
	extensionMap.put("elc", "application/x-bytecode.elisp (compiled elisp)");
	extensionMap.put("elc", "application/x-elc");
	extensionMap.put("env", "application/x-envoy");
	extensionMap.put("eps", "application/postscript");
	extensionMap.put("es", "application/x-esrehber");
	extensionMap.put("etx", "text/x-setext");
	extensionMap.put("evy", "application/envoy");
	extensionMap.put("evy", "application/x-envoy");
	extensionMap.put("exe", "application/octet-stream");
	extensionMap.put("f", "text/plain");
	extensionMap.put("f", "text/x-fortran");
	extensionMap.put("f77", "text/x-fortran");
	extensionMap.put("f90", "text/plain");
	extensionMap.put("f90", "text/x-fortran");
	extensionMap.put("fdf", "application/vnd.fdf");
	extensionMap.put("fif", "application/fractals");
	extensionMap.put("fif", "image/fif");
	extensionMap.put("fli", "video/fli");
	extensionMap.put("fli", "video/x-fli");
	extensionMap.put("flo", "image/florian");
	extensionMap.put("flx", "text/vnd.fmi.flexstor");
	extensionMap.put("fmf", "video/x-atomic3d-feature");
	extensionMap.put("for", "text/plain");
	extensionMap.put("for", "text/x-fortran");
	extensionMap.put("fpx", "image/vnd.fpx");
	extensionMap.put("fpx", "image/vnd.net-fpx");
	extensionMap.put("frl", "application/freeloader");
	extensionMap.put("funk", "audio/make");
	extensionMap.put("g", "text/plain");
	extensionMap.put("g3", "image/g3fax");
	extensionMap.put("gif", "image/gif");
	extensionMap.put("gl", "video/gl");
	extensionMap.put("gl", "video/x-gl");
	extensionMap.put("gsd", "audio/x-gsm");
	extensionMap.put("gsm", "audio/x-gsm");
	extensionMap.put("gsp", "application/x-gsp");
	extensionMap.put("gss", "application/x-gss");
	extensionMap.put("gtar", "application/x-gtar");
	extensionMap.put("gz", "application/x-compressed");
	extensionMap.put("gz", "application/x-gzip");
	extensionMap.put("gzip", "application/x-gzip");
	extensionMap.put("gzip", "multipart/x-gzip");
	extensionMap.put("h", "text/plain");
	extensionMap.put("h", "text/x-h");
	extensionMap.put("hdf", "application/x-hdf");
	extensionMap.put("help", "application/x-helpfile");
	extensionMap.put("hgl", "application/vnd.hp-hpgl");
	extensionMap.put("hh", "text/plain");
	extensionMap.put("hh", "text/x-h");
	extensionMap.put("hlb", "text/x-script");
	extensionMap.put("hlp", "application/hlp");
	extensionMap.put("hlp", "application/x-helpfile");
	extensionMap.put("hlp", "application/x-winhelp");
	extensionMap.put("hpg", "application/vnd.hp-hpgl");
	extensionMap.put("hpgl", "application/vnd.hp-hpgl");
	extensionMap.put("hqx", "application/binhex");
	extensionMap.put("hqx", "application/binhex4");
	extensionMap.put("hqx", "application/mac-binhex");
	extensionMap.put("hqx", "application/mac-binhex40");
	extensionMap.put("hqx", "application/x-binhex40");
	extensionMap.put("hqx", "application/x-mac-binhex40");
	extensionMap.put("hta", "application/hta");
	extensionMap.put("htc", "text/x-component");
	extensionMap.put("htm", "text/html");
	extensionMap.put("html", "text/html");
	extensionMap.put("htmls", "text/html");
	extensionMap.put("htt", "text/webviewhtml");
	extensionMap.put("htx", "text/html");
	extensionMap.put("ice", "x-conference/x-cooltalk");
	extensionMap.put("ico", "image/x-icon");
	extensionMap.put("idc", "text/plain");
	extensionMap.put("ief", "image/ief");
	extensionMap.put("iefs", "image/ief");
	extensionMap.put("iges", "application/iges");
	extensionMap.put("iges", "model/iges");
	extensionMap.put("igs", "application/iges");
	extensionMap.put("igs", "model/iges");
	extensionMap.put("ima", "application/x-ima");
	extensionMap.put("imap", "application/x-httpd-imap");
	extensionMap.put("inf", "application/inf");
	extensionMap.put("ins", "application/x-internett-signup");
	extensionMap.put("ip", "application/x-ip2");
	extensionMap.put("isu", "video/x-isvideo");
	extensionMap.put("it", "audio/it");
	extensionMap.put("iv", "application/x-inventor");
	extensionMap.put("ivr", "i-world/i-vrml");
	extensionMap.put("ivy", "application/x-livescreen");
	extensionMap.put("jam", "audio/x-jam");
	extensionMap.put("jav", "text/plain");
	extensionMap.put("jav", "text/x-java-source");
	extensionMap.put("java", "text/plain");
	extensionMap.put("java", "text/x-java-source");
	extensionMap.put("jcm", "application/x-java-commerce");
	extensionMap.put("jfif", "image/jpeg");
	extensionMap.put("jfif", "image/pjpeg");
	extensionMap.put("jfif-tbnl", "image/jpeg");
	extensionMap.put("jpe", "image/jpeg");
	extensionMap.put("jpe", "image/pjpeg");
	extensionMap.put("jpeg", "image/jpeg");
	extensionMap.put("jpeg", "image/pjpeg");
	extensionMap.put("jpg", "image/jpeg");
	extensionMap.put("jpg", "image/pjpeg");
	extensionMap.put("jps", "image/x-jps");
	extensionMap.put("js", "application/x-javascript");
	extensionMap.put("jut", "image/jutvision");
	extensionMap.put("kar", "audio/midi");
	extensionMap.put("kar", "music/x-karaoke");
	extensionMap.put("ksh", "application/x-ksh");
	extensionMap.put("ksh", "text/x-script.ksh");
	extensionMap.put("la", "audio/nspaudio");
	extensionMap.put("la", "audio/x-nspaudio");
	extensionMap.put("lam", "audio/x-liveaudio");
	extensionMap.put("latex", "application/x-latex");
	extensionMap.put("lha", "application/lha");
	extensionMap.put("lha", "application/octet-stream");
	extensionMap.put("lha", "application/x-lha");
	extensionMap.put("lhx", "application/octet-stream");
	extensionMap.put("list", "text/plain");
	extensionMap.put("lma", "audio/nspaudio");
	extensionMap.put("lma", "audio/x-nspaudio");
	extensionMap.put("log", "text/plain");
	extensionMap.put("lsp", "application/x-lisp");
	extensionMap.put("lsp", "text/x-script.lisp");
	extensionMap.put("lst", "text/plain");
	extensionMap.put("lsx", "text/x-la-asf");
	extensionMap.put("ltx", "application/x-latex");
	extensionMap.put("lzh", "application/octet-stream");
	extensionMap.put("lzh", "application/x-lzh");
	extensionMap.put("lzx", "application/lzx");
	extensionMap.put("lzx", "application/octet-stream");
	extensionMap.put("lzx", "application/x-lzx");
	extensionMap.put("m", "text/plain");
	extensionMap.put("m", "text/x-m");
	extensionMap.put("m1v", "video/mpeg");
	extensionMap.put("m2a", "audio/mpeg");
	extensionMap.put("m2v", "video/mpeg");
	extensionMap.put("m3u", "audio/x-mpequrl");
	extensionMap.put("man", "application/x-troff-man");
	extensionMap.put("map", "application/x-navimap");
	extensionMap.put("mar", "text/plain");
	extensionMap.put("mbd", "application/mbedlet");
	extensionMap.put("mc$", "application/x-magic-cap-package-1.0");
	extensionMap.put("mcd", "application/mcad");
	extensionMap.put("mcd", "application/x-mathcad");
	extensionMap.put("mcf", "image/vasa");
	extensionMap.put("mcf", "text/mcf");
	extensionMap.put("mcp", "application/netmc");
	extensionMap.put("me", "application/x-troff-me");
	extensionMap.put("mht", "message/rfc822");
	extensionMap.put("mhtml", "message/rfc822");
	extensionMap.put("mid", "application/x-midi");
	extensionMap.put("mid", "audio/midi");
	extensionMap.put("mid", "audio/x-mid");
	extensionMap.put("mid", "audio/x-midi");
	extensionMap.put("mid", "music/crescendo");
	extensionMap.put("mid", "x-music/x-midi");
	extensionMap.put("midi", "application/x-midi");
	extensionMap.put("midi", "audio/midi");
	extensionMap.put("midi", "audio/x-mid");
	extensionMap.put("midi", "audio/x-midi");
	extensionMap.put("midi", "music/crescendo");
	extensionMap.put("midi", "x-music/x-midi");
	extensionMap.put("mif", "application/x-frame");
	extensionMap.put("mif", "application/x-mif");
	extensionMap.put("mime", "message/rfc822");
	extensionMap.put("mime", "www/mime");
	extensionMap.put("mjf", "audio/x-vnd.audioexplosion.mjuicemediafile");
	extensionMap.put("mjpg", "video/x-motion-jpeg");
	extensionMap.put("mm", "application/base64");
	extensionMap.put("mm", "application/x-meme");
	extensionMap.put("mme", "application/base64");
	extensionMap.put("mod", "audio/mod");
	extensionMap.put("mod", "audio/x-mod");
	extensionMap.put("moov", "video/quicktime");
	extensionMap.put("mov", "video/quicktime");
	extensionMap.put("movie", "video/x-sgi-movie");
	extensionMap.put("mp2", "audio/mpeg");
	extensionMap.put("mp2", "audio/x-mpeg");
	extensionMap.put("mp2", "video/mpeg");
	extensionMap.put("mp2", "video/x-mpeg");
	extensionMap.put("mp2", "video/x-mpeq2a");
	extensionMap.put("mp3", "audio/mpeg3");
	extensionMap.put("mp3", "audio/x-mpeg-3");
	extensionMap.put("mp3", "video/mpeg");
	extensionMap.put("mp3", "video/x-mpeg");
	extensionMap.put("mpa", "audio/mpeg");
	extensionMap.put("mpa", "video/mpeg");
	extensionMap.put("mpc", "application/x-project");
	extensionMap.put("mpe", "video/mpeg");
	extensionMap.put("mpeg", "video/mpeg");
	extensionMap.put("mpg", "audio/mpeg");
	extensionMap.put("mpg", "video/mpeg");
	extensionMap.put("mpga", "audio/mpeg");
	extensionMap.put("mpp", "application/vnd.ms-project");
	extensionMap.put("mpt", "application/x-project");
	extensionMap.put("mpv", "application/x-project");
	extensionMap.put("mpx", "application/x-project");
	extensionMap.put("mrc", "application/marc");
	extensionMap.put("ms", "application/x-troff-ms");
	extensionMap.put("mv", "video/x-sgi-movie");
	extensionMap.put("my", "audio/make");
	extensionMap.put("mzz", "application/x-vnd.audioexplosion.mzz");
	extensionMap.put("nap", "image/naplps");
	extensionMap.put("naplps", "image/naplps");
	extensionMap.put("nc", "application/x-netcdf");
	extensionMap.put("ncm", "application/vnd.nokia.configuration-message");
	extensionMap.put("nif", "image/x-niff");
	extensionMap.put("niff", "image/x-niff");
	extensionMap.put("nix", "application/x-mix-transfer");
	extensionMap.put("nsc", "application/x-conference");
	extensionMap.put("nvd", "application/x-navidoc");
	extensionMap.put("o", "application/octet-stream");
	extensionMap.put("oda", "application/oda");
	extensionMap.put("omc", "application/x-omc");
	extensionMap.put("omcd", "application/x-omcdatamaker");
	extensionMap.put("omcr", "application/x-omcregerator");
	extensionMap.put("p", "text/x-pascal");
	extensionMap.put("p10", "application/pkcs10");
	extensionMap.put("p10", "application/x-pkcs10");
	extensionMap.put("p12", "application/pkcs-12");
	extensionMap.put("p12", "application/x-pkcs12");
	extensionMap.put("p7a", "application/x-pkcs7-signature");
	extensionMap.put("p7c", "application/pkcs7-mime");
	extensionMap.put("p7c", "application/x-pkcs7-mime");
	extensionMap.put("p7m", "application/pkcs7-mime");
	extensionMap.put("p7m", "application/x-pkcs7-mime");
	extensionMap.put("p7r", "application/x-pkcs7-certreqresp");
	extensionMap.put("p7s", "application/pkcs7-signature");
	extensionMap.put("part", "application/pro_eng");
	extensionMap.put("pas", "text/pascal");
	extensionMap.put("pbm", "image/x-portable-bitmap");
	extensionMap.put("pcl", "application/vnd.hp-pcl");
	extensionMap.put("pcl", "application/x-pcl");
	extensionMap.put("pct", "image/x-pict");
	extensionMap.put("pcx", "image/x-pcx");
	extensionMap.put("pdb", "chemical/x-pdb");
	extensionMap.put("pdf", "application/pdf");
	extensionMap.put("pfunk", "audio/make");
	extensionMap.put("pfunk", "audio/make.my.funk");
	extensionMap.put("pgm", "image/x-portable-graymap");
	extensionMap.put("pgm", "image/x-portable-greymap");
	extensionMap.put("pic", "image/pict");
	extensionMap.put("pict", "image/pict");
	extensionMap.put("pkg", "application/x-newton-compatible-pkg");
	extensionMap.put("pko", "application/vnd.ms-pki.pko");
	extensionMap.put("pl", "text/plain");
	extensionMap.put("pl", "text/x-script.perl");
	extensionMap.put("plx", "application/x-pixclscript");
	extensionMap.put("pm", "image/x-xpixmap");
	extensionMap.put("pm", "text/x-script.perl-module");
	extensionMap.put("pm4", "application/x-pagemaker");
	extensionMap.put("pm5", "application/x-pagemaker");
	extensionMap.put("png", "image/png");
	extensionMap.put("pnm", "application/x-portable-anymap");
	extensionMap.put("pnm", "image/x-portable-anymap");
	extensionMap.put("pot", "application/mspowerpoint");
	extensionMap.put("pot", "application/vnd.ms-powerpoint");
	extensionMap.put("pov", "model/x-pov");
	extensionMap.put("ppa", "application/vnd.ms-powerpoint");
	extensionMap.put("ppm", "image/x-portable-pixmap");
	extensionMap.put("pps", "application/mspowerpoint");
	extensionMap.put("pps", "application/vnd.ms-powerpoint");
	extensionMap.put("ppt", "application/mspowerpoint");
	extensionMap.put("ppt", "application/powerpoint");
	extensionMap.put("ppt", "application/vnd.ms-powerpoint");
	extensionMap.put("ppt", "application/x-mspowerpoint");
	extensionMap.put("ppz", "application/mspowerpoint");
	extensionMap.put("pre", "application/x-freelance");
	extensionMap.put("prt", "application/pro_eng");
	extensionMap.put("ps", "application/postscript");
	extensionMap.put("psd", "application/octet-stream");
	extensionMap.put("pvu", "paleovu/x-pv");
	extensionMap.put("pwz", "application/vnd.ms-powerpoint");
	extensionMap.put("py", "text/x-script.phyton");
	extensionMap.put("pyc", "applicaiton/x-bytecode.python");
	extensionMap.put("qcp", "audio/vnd.qcelp");
	extensionMap.put("qd3", "x-world/x-3dmf");
	extensionMap.put("qd3d", "x-world/x-3dmf");
	extensionMap.put("qif", "image/x-quicktime");
	extensionMap.put("qt", "video/quicktime");
	extensionMap.put("qtc", "video/x-qtc");
	extensionMap.put("qti", "image/x-quicktime");
	extensionMap.put("qtif", "image/x-quicktime");
	extensionMap.put("ra", "audio/x-pn-realaudio");
	extensionMap.put("ra", "audio/x-pn-realaudio-plugin");
	extensionMap.put("ra", "audio/x-realaudio");
	extensionMap.put("ram", "audio/x-pn-realaudio");
	extensionMap.put("ras", "application/x-cmu-raster");
	extensionMap.put("ras", "image/cmu-raster");
	extensionMap.put("ras", "image/x-cmu-raster");
	extensionMap.put("rast", "image/cmu-raster");
	extensionMap.put("rexx", "text/x-script.rexx");
	extensionMap.put("rf", "image/vnd.rn-realflash");
	extensionMap.put("rgb", "image/x-rgb");
	extensionMap.put("rm", "application/vnd.rn-realmedia");
	extensionMap.put("rm", "audio/x-pn-realaudio");
	extensionMap.put("rmi", "audio/mid");
	extensionMap.put("rmm", "audio/x-pn-realaudio");
	extensionMap.put("rmp", "audio/x-pn-realaudio");
	extensionMap.put("rmp", "audio/x-pn-realaudio-plugin");
	extensionMap.put("rng", "application/ringing-tones");
	extensionMap.put("rng", "application/vnd.nokia.ringing-tone");
	extensionMap.put("rnx", "application/vnd.rn-realplayer");
	extensionMap.put("roff", "application/x-troff");
	extensionMap.put("rp", "image/vnd.rn-realpix");
	extensionMap.put("rpm", "audio/x-pn-realaudio-plugin");
	extensionMap.put("rt", "text/richtext");
	extensionMap.put("rt", "text/vnd.rn-realtext");
	extensionMap.put("rtf", "application/rtf");
	extensionMap.put("rtf", "application/x-rtf");
	extensionMap.put("rtf", "text/richtext");
	extensionMap.put("rtx", "application/rtf");
	extensionMap.put("rtx", "text/richtext");
	extensionMap.put("rv", "video/vnd.rn-realvideo");
	extensionMap.put("s", "text/x-asm");
	extensionMap.put("s3m", "audio/s3m");
	extensionMap.put("saveme", "application/octet-stream");
	extensionMap.put("sbk", "application/x-tbook");
	extensionMap.put("scm", "application/x-lotusscreencam");
	extensionMap.put("scm", "text/x-script.guile");
	extensionMap.put("scm", "text/x-script.scheme");
	extensionMap.put("scm", "video/x-scm");
	extensionMap.put("sdml", "text/plain");
	extensionMap.put("sdp", "application/sdp");
	extensionMap.put("sdp", "application/x-sdp");
	extensionMap.put("sdr", "application/sounder");
	extensionMap.put("sea", "application/sea");
	extensionMap.put("sea", "application/x-sea");
	extensionMap.put("set", "application/set");
	extensionMap.put("sgm", "text/sgml");
	extensionMap.put("sgm", "text/x-sgml");
	extensionMap.put("sgml", "text/sgml");
	extensionMap.put("sgml", "text/x-sgml");
	extensionMap.put("sh", "application/x-bsh");
	extensionMap.put("sh", "application/x-sh");
	extensionMap.put("sh", "application/x-shar");
	extensionMap.put("sh", "text/x-script.sh");
	extensionMap.put("shar", "application/x-bsh");
	extensionMap.put("shar", "application/x-shar");
	extensionMap.put("shtml", "text/html");
	extensionMap.put("shtml", "text/x-server-parsed-html");
	extensionMap.put("sid", "audio/x-psid");
	extensionMap.put("sit", "application/x-sit");
	extensionMap.put("sit", "application/x-stuffit");
	extensionMap.put("skd", "application/x-koan");
	extensionMap.put("skm", "application/x-koan");
	extensionMap.put("skp", "application/x-koan");
	extensionMap.put("skt", "application/x-koan");
	extensionMap.put("sl", "application/x-seelogo");
	extensionMap.put("smi", "application/smil");
	extensionMap.put("smil", "application/smil");
	extensionMap.put("snd", "audio/basic");
	extensionMap.put("snd", "audio/x-adpcm");
	extensionMap.put("sol", "application/solids");
	extensionMap.put("spc", "application/x-pkcs7-certificates");
	extensionMap.put("spc", "text/x-speech");
	extensionMap.put("spl", "application/futuresplash");
	extensionMap.put("spr", "application/x-sprite");
	extensionMap.put("sprite", "application/x-sprite");
	extensionMap.put("src", "application/x-wais-source");
	extensionMap.put("ssi", "text/x-server-parsed-html");
	extensionMap.put("ssm", "application/streamingmedia");
	extensionMap.put("sst", "application/vnd.ms-pki.certstore");
	extensionMap.put("step", "application/step");
	extensionMap.put("stl", "application/sla");
	extensionMap.put("stl", "application/vnd.ms-pki.stl");
	extensionMap.put("stl", "application/x-navistyle");
	extensionMap.put("stp", "application/step");
	extensionMap.put("sv4cpio", "application/x-sv4cpio");
	extensionMap.put("sv4crc", "application/x-sv4crc");
	extensionMap.put("svf", "image/vnd.dwg");
	extensionMap.put("svf", "image/x-dwg");
	extensionMap.put("svr", "application/x-world");
	extensionMap.put("svr", "x-world/x-svr");
	extensionMap.put("swf", "application/x-shockwave-flash");
	extensionMap.put("t", "application/x-troff");
	extensionMap.put("talk", "text/x-speech");
	extensionMap.put("tar", "application/x-tar");
	extensionMap.put("tbk", "application/toolbook");
	extensionMap.put("tbk", "application/x-tbook");
	extensionMap.put("tcl", "application/x-tcl");
	extensionMap.put("tcl", "text/x-script.tcl");
	extensionMap.put("tcsh", "text/x-script.tcsh");
	extensionMap.put("tex", "application/x-tex");
	extensionMap.put("texi", "application/x-texinfo");
	extensionMap.put("texinfo", "application/x-texinfo");
	extensionMap.put("text", "application/plain");
	extensionMap.put("text", "text/plain");
	extensionMap.put("tgz", "application/gnutar");
	extensionMap.put("tgz", "application/x-compressed");
	extensionMap.put("tif", "image/tiff");
	extensionMap.put("tif", "image/x-tiff");
	extensionMap.put("tiff", "image/tiff");
	extensionMap.put("tiff", "image/x-tiff");
	extensionMap.put("tr", "application/x-troff");
	extensionMap.put("tsi", "audio/tsp-audio");
	extensionMap.put("tsp", "application/dsptype");
	extensionMap.put("tsp", "audio/tsplayer");
	extensionMap.put("tsv", "text/tab-separated-values");
	extensionMap.put("turbot", "image/florian");
	extensionMap.put("txt", "text/plain");
	extensionMap.put("uil", "text/x-uil");
	extensionMap.put("uni", "text/uri-list");
	extensionMap.put("unis", "text/uri-list");
	extensionMap.put("unv", "application/i-deas");
	extensionMap.put("uri", "text/uri-list");
	extensionMap.put("uris", "text/uri-list");
	extensionMap.put("ustar", "application/x-ustar");
	extensionMap.put("ustar", "multipart/x-ustar");
	extensionMap.put("uu", "application/octet-stream");
	extensionMap.put("uu", "text/x-uuencode");
	extensionMap.put("uue", "text/x-uuencode");
	extensionMap.put("vcd", "application/x-cdlink");
	extensionMap.put("vcs", "text/x-vcalendar");
	extensionMap.put("vda", "application/vda");
	extensionMap.put("vdo", "video/vdo");
	extensionMap.put("vew", "application/groupwise");
	extensionMap.put("viv", "video/vivo");
	extensionMap.put("viv", "video/vnd.vivo");
	extensionMap.put("vivo", "video/vivo");
	extensionMap.put("vivo", "video/vnd.vivo");
	extensionMap.put("vmd", "application/vocaltec-media-desc");
	extensionMap.put("vmf", "application/vocaltec-media-file");
	extensionMap.put("voc", "audio/voc");
	extensionMap.put("voc", "audio/x-voc");
	extensionMap.put("vos", "video/vosaic");
	extensionMap.put("vox", "audio/voxware");
	extensionMap.put("vqe", "audio/x-twinvq-plugin");
	extensionMap.put("vqf", "audio/x-twinvq");
	extensionMap.put("vql", "audio/x-twinvq-plugin");
	extensionMap.put("vrml", "application/x-vrml");
	extensionMap.put("vrml", "model/vrml");
	extensionMap.put("vrml", "x-world/x-vrml");
	extensionMap.put("vrt", "x-world/x-vrt");
	extensionMap.put("vsd", "application/x-visio");
	extensionMap.put("vst", "application/x-visio");
	extensionMap.put("vsw", "application/x-visio");
	extensionMap.put("w60", "application/wordperfect6.0");
	extensionMap.put("w61", "application/wordperfect6.1");
	extensionMap.put("w6w", "application/msword");
	extensionMap.put("wav", "audio/wav");
	extensionMap.put("wav", "audio/x-wav");
	extensionMap.put("wb1", "application/x-qpro");
	extensionMap.put("wbmp", "image/vnd.wap.wbmp");
	extensionMap.put("web", "application/vnd.xara");
	extensionMap.put("wiz", "application/msword");
	extensionMap.put("wk1", "application/x-123");
	extensionMap.put("wmf", "windows/metafile");
	extensionMap.put("wml", "text/vnd.wap.wml");
	extensionMap.put("wmlc", "application/vnd.wap.wmlc");
	extensionMap.put("wmls", "text/vnd.wap.wmlscript");
	extensionMap.put("wmlsc", "application/vnd.wap.wmlscriptc");
	extensionMap.put("word", "application/msword");
	extensionMap.put("wp", "application/wordperfect");
	extensionMap.put("wp5", "application/wordperfect");
	extensionMap.put("wp5", "application/wordperfect6.0");
	extensionMap.put("wp6", "application/wordperfect");
	extensionMap.put("wpd", "application/wordperfect");
	extensionMap.put("wpd", "application/x-wpwin");
	extensionMap.put("wq1", "application/x-lotus");
	extensionMap.put("wri", "application/mswrite");
	extensionMap.put("wri", "application/x-wri");
	extensionMap.put("wrl", "application/x-world");
	extensionMap.put("wrl", "model/vrml");
	extensionMap.put("wrl", "x-world/x-vrml");
	extensionMap.put("wrz", "model/vrml");
	extensionMap.put("wrz", "x-world/x-vrml");
	extensionMap.put("wsc", "text/scriplet");
	extensionMap.put("wsrc", "application/x-wais-source");
	extensionMap.put("wtk", "application/x-wintalk");
	extensionMap.put("xbm", "image/x-xbitmap");
	extensionMap.put("xbm", "image/x-xbm");
	extensionMap.put("xbm", "image/xbm");
	extensionMap.put("xdr", "video/x-amt-demorun");
	extensionMap.put("xgz", "xgl/drawing");
	extensionMap.put("xhtml", "application/xhtml+xml" );
	extensionMap.put("xif", "image/vnd.xiff");
	extensionMap.put("xl", "application/excel");
	extensionMap.put("xla", "application/excel");
	extensionMap.put("xla", "application/x-excel");
	extensionMap.put("xla", "application/x-msexcel");
	extensionMap.put("xlb", "application/excel");
	extensionMap.put("xlb", "application/vnd.ms-excel");
	extensionMap.put("xlb", "application/x-excel");
	extensionMap.put("xlc", "application/excel");
	extensionMap.put("xlc", "application/vnd.ms-excel");
	extensionMap.put("xlc", "application/x-excel");
	extensionMap.put("xld", "application/excel");
	extensionMap.put("xld", "application/x-excel");
	extensionMap.put("xlk", "application/excel");
	extensionMap.put("xlk", "application/x-excel");
	extensionMap.put("xll", "application/excel");
	extensionMap.put("xll", "application/vnd.ms-excel");
	extensionMap.put("xll", "application/x-excel");
	extensionMap.put("xlm", "application/excel");
	extensionMap.put("xlm", "application/vnd.ms-excel");
	extensionMap.put("xlm", "application/x-excel");
	extensionMap.put("xls", "application/excel");
	extensionMap.put("xls", "application/vnd.ms-excel");
	extensionMap.put("xls", "application/x-excel");
	extensionMap.put("xls", "application/x-msexcel");
	extensionMap.put("xlt", "application/excel");
	extensionMap.put("xlt", "application/x-excel");
	extensionMap.put("xlv", "application/excel");
	extensionMap.put("xlv", "application/x-excel");
	extensionMap.put("xlw", "application/excel");
	extensionMap.put("xlw", "application/vnd.ms-excel");
	extensionMap.put("xlw", "application/x-excel");
	extensionMap.put("xlw", "application/x-msexcel");
	extensionMap.put("xm", "audio/xm");
	extensionMap.put("xml", "application/xml");
	extensionMap.put("xml", "text/xml");
	extensionMap.put("xmz", "xgl/movie");
	extensionMap.put("xpix", "application/x-vnd.ls-xpix");
	extensionMap.put("xpm", "image/x-xpixmap");
	extensionMap.put("xpm", "image/xpm");
	extensionMap.put("x-png", "image/png");
	extensionMap.put("xsr", "video/x-amt-showrun");
	extensionMap.put("xwd", "image/x-xwd");
	extensionMap.put("xwd", "image/x-xwindowdump");
	extensionMap.put("xyz", "chemical/x-pdb");
	extensionMap.put("z", "application/x-compress");
	extensionMap.put("z", "application/x-compressed");
	extensionMap.put("zip", "application/x-compressed");
	extensionMap.put("zip", "application/x-zip-compressed");
	extensionMap.put("zip", "application/zip");
	extensionMap.put("zip", "multipart/x-zip");
	extensionMap.put("zoo", "application/octet-stream");
	extensionMap.put("zsh", "text/x-script.zsh");
    }

    private String contentType;

    /**
     * The consutructor.
     * The type param will not be validatet!
     **/
    public MIMEType( String type ) {
	this.contentType = type;
    }
    
    /**
     * Get the full content-type string.
     **/
    public String getContentType() {
	return this.contentType;
    }

    public String toString() {
	return getClass().getName() + "=[ contentType=" + this.contentType + " ]";
    }

    /**
     * This method tries to create a new MIME type from the given file extension (without 
     * the dot '.'). If the file extension cannot be found in the internal mapping the method
     * returns null.
     *
     * @param extension The filename extension (search is not case sensitive).
     **/
    public static MIMEType getByFileExtension( String extension ) {
	String mimeType = extensionMap.get(extension);
	if( mimeType == null )
	    return null;
	else
	    return new MIMEType( mimeType );
    }


}