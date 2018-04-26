package com.excilys.cdb.taglibs;

import java.io.Writer;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PaginationLib extends SimpleTagSupport {

    private int currentPage;
    private int numberOfElement;
    private int elementPerPage;
    private String uri;

    /**
     * Récupère le writer de la jsp.
     * @return le writer
     */
    private Writer getWriter() {
        return getJspContext().getOut();
    }

    /**
     * Affiche les différents éléments de pagination.
     *
     */
    @Override
    public void doTag() {
        Writer writer = getWriter();
        double numberPage = (double) numberOfElement / (double) elementPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        int startPage = Math.max(currentPage - elementPerPage / 2, 1);
        int endPage = startPage + elementPerPage;
        if (endPage > numberOfPage) {
            int diff = endPage - numberOfPage;
            startPage -= diff - 1;
            if (startPage < 1) {
                startPage = 1;
            }
            endPage = numberOfPage + 1;
        }
        try {
            writer.write("<ul class=\"pagination\">");
            if (currentPage > 1) {
                writer.write(createLink(currentPage - 1, "<<"));
            } else {
                writer.write("<li><a disabled><<</a>");
            }
            for (int i = startPage; i < endPage; i++) {
                if (i == currentPage) {
                    writer.write("<li style=\"font-weight: bold; \"><a>" + currentPage + "</a>");
                } else {
                    writer.write(createLink(i, String.valueOf(i)));
                }
            }
            if (currentPage < numberOfPage) {
                writer.write(createLink(currentPage + 1, ">>"));
            } else {
                writer.write("<li><a disabled>>></a>");
            }
            writer.write("</ul>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les liens vers le servlet avec les bons paramètres.
     * @param page La page à afficher
     * @param name Le texte affiché dans la pagination
     * @return L'url vers la page
     */
    public String createLink(int page, String name) {
        StringBuilder link = new StringBuilder("<li");
        link.append(">").append("<a href=\"").append(uri + "&page=" + String.valueOf(page)).append("\">").append(name)
        .append("</a></li>");
        return link.toString();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumberOfElement() {
        return numberOfElement;
    }

    public void setNumberOfElement(int numberOfElement) {
        this.numberOfElement = numberOfElement;
    }

    public int getElementPerPage() {
        return elementPerPage;
    }

    public void setElementPerPage(int elementPerPage) {
        this.elementPerPage = elementPerPage;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
