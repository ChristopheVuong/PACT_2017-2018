package main.client.utils;

public final class FileComboItem {

    private String MD5;
    private String fileName;

    public FileComboItem(final String MD5, final String fileName) {
        this.MD5 = MD5;
        this.fileName = fileName;
    }

    public FileComboItem(final String[] MD5AndFileName) {
        if(MD5AndFileName.length == 2) {
            this.MD5 = MD5AndFileName[0];
            this.fileName = MD5AndFileName[1];
        }
    }

    public String getMD5() {
        return MD5;
    }

    public String getFileName() {
        return fileName;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}