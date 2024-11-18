package io.micronaut.guides.core.asciidoc;

import io.micronaut.context.annotation.ConfigurationProperties;
import org.asciidoctor.Placement;

import java.io.File;

@ConfigurationProperties(AsciidocConfigurationProperties.PREFIX)
public class AsciidocConfigurationProperties implements AsciidocConfiguration {
    public static final String PREFIX = "asciidoc";
    private static final String DEFAULT_SOURCEDIR = "build/code";
    private static final String DEFAULT_SOURCE_HIGHLIGHTER = "coderay";
    private static final Placement DEFAULT_TOC = Placement.LEFT;
    private static final int DEFAULT_TOCLEVELS = 2;
    private static final boolean DEFAULT_SECTNUMS = true;
    private static final String DEFAULT_IDPREFIX = "";
    private static final String DEFAULT_IDSEPARATOR = "-";
    private static final String DEFAULT_ICONS = "font";
    private static final String DEFAULT_IMAGESDIR = "images";
    private static final boolean DEFAULT_NOFOOTER = true;
    private static final String DEFAULT_DOCTYPE = "book";
    private static final String DEFAULT_RUBY = "erubis";
    private static final String DEFAULT_TEMPLATE_DIRS = "src/docs/asciidoc";
    private static final String DEFAULT_COMMONS_DIR = "src/docs/asciidoc/common";
    private static final String DEFAULT_CALLOUTS_DIR = "src/docs/asciidoc/callouts";
    private static final String DEFAULT_BASE_DIR = ".";
    private static final String DEFAULT_GUIDES_DIR = "guides";

    private String sourcedir = DEFAULT_SOURCEDIR;
    private String sourceHighlighter = DEFAULT_SOURCE_HIGHLIGHTER;
    private Placement toc = DEFAULT_TOC;
    private int toclevels = DEFAULT_TOCLEVELS;
    private boolean sectnums = DEFAULT_SECTNUMS;
    private String idprefix = DEFAULT_IDPREFIX;
    private String idseparator = DEFAULT_IDSEPARATOR;
    private String icons = DEFAULT_ICONS;
    private String imagesdir = DEFAULT_IMAGESDIR;
    private boolean nofooter = DEFAULT_NOFOOTER;
    private String docType = DEFAULT_DOCTYPE;
    private String ruby = DEFAULT_RUBY;
    private File templateDirs = new File(DEFAULT_TEMPLATE_DIRS);
    private String commonsDir = DEFAULT_COMMONS_DIR;
    private String baseDir = DEFAULT_BASE_DIR;
    private String guidesDir = DEFAULT_GUIDES_DIR;
    private String calloutsDir = DEFAULT_CALLOUTS_DIR;

    @Override
    public String getSourceDir() {
        return sourcedir;
    }

    public void setSourceDir(String sourcedir) {
        this.sourcedir = sourcedir;
    }

    @Override
    public String getSourceHighlighter() {
        return sourceHighlighter;
    }

    public void setSourceHighlighter(String sourceHighlighter) {
        this.sourceHighlighter = sourceHighlighter;
    }

    @Override
    public Placement getToc() {
        return toc;
    }

    public void setToc(Placement toc) {
        this.toc = toc;
    }

    @Override
    public int getToclevels() {
        return toclevels;
    }

    public void setToclevels(int toclevels) {
        this.toclevels = toclevels;
    }

    @Override
    public boolean getSectnums() {
        return sectnums;
    }

    public void setSectnums(boolean sectnums) {
        this.sectnums = sectnums;
    }

    @Override
    public String getIdprefix() {
        return idprefix;
    }

    public void setIdprefix(String idprefix) {
        this.idprefix = idprefix;
    }

    @Override
    public String getIdseparator() {
        return idseparator;
    }

    public void setIdseparator(String idseparator) {
        this.idseparator = idseparator;
    }

    @Override
    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }

    @Override
    public String getImagesdir() {
        return imagesdir;
    }

    public void setImagesdir(String imagesdir) {
        this.imagesdir = imagesdir;
    }

    @Override
    public boolean isNofooter() {
        return nofooter;
    }

    public void setNofooter(boolean nofooter) {
        this.nofooter = nofooter;
    }

    @Override
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Override
    public String getRuby() {
        return ruby;
    }

    public void setRuby(String ruby) {
        this.ruby = ruby;
    }

    @Override
    public File getTemplateDirs() {
        return templateDirs;
    }

    public void setTemplateDirs(File templateDirs) {
        this.templateDirs = templateDirs;
    }

    @Override
    public String getCommonsDir() {
        return commonsDir;
    }

    public void setCommonsDir(String commonsDir) {
        this.commonsDir = commonsDir;
    }

    @Override
    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public String getGuidesDir() {
        return guidesDir;
    }

    public void setGuidesDir(String guidesDir) {
        this.guidesDir = guidesDir;
    }

    @Override
    public String getCalloutsDir() {
        return calloutsDir;
    }

    public void setCalloutsDir(String calloutsDir) {
        this.calloutsDir = calloutsDir;
    }
}