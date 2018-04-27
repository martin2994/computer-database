package com.excilys.cdb.taglibs;

import java.io.Writer;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.services.Facade;

public class PaginationLib extends SimpleTagSupport {

    private int currentPage;
    private int numberOfElement;
    private int elementPerPage;
    private String uri;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Facade.class);

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
        // le nombre de page
        double numberPage = (double) numberOfElement / (double) elementPerPage;
        int numberOfPage = (int) Math.ceil(numberPage);
        if (currentPage > numberOfPage) {
            currentPage = numberOfPage;
        }
        // calcule les pages à afficher
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
            // affichage des first < et previous <<
            if (currentPage > 1) {
                writer.write(createLink(1, "<<"));
                writer.write(createLink(currentPage - 1, "<"));
            } else {
                writer.write("<li><a disabled><<</a></li>");
                writer.write("<li><a disabled><</a></li>");
            }
            // affichage des différentes pages visibles
            for (int i = startPage; i < endPage; i++) {
                if (i == currentPage) {
                    writer.write("<li class=\"active\"><a>" + currentPage + "</a></li>");
                } else {
                    writer.write(createLink(i, String.valueOf(i)));
                }
            }
            // affichage des next > et last >>
            if (currentPage < numberOfPage) {
                writer.write(createLink(currentPage + 1, ">"));
                writer.write(createLink(numberOfPage, ">>"));
            } else {
                writer.write("<li><a disabled>></a></li>");
                writer.write("<li><a disabled>>></a></li>");
            }
            writer.write("</ul>");
        } catch (Exception e) {
            LOGGER.debug("PAGINATION LIB ERROR " + e.getMessage());
        }
    }

    /**
     * Crée les liens vers la servlet avec les bons paramètres.
     * @param page
     *            La page à afficher
     * @param name
     *            Le texte affiché dans la pagination
     * @return L'url vers la page
     */
    public String createLink(int page, String name) {
        StringBuilder link = new StringBuilder("<li>");
        link.append("<a href=\"").append(uri + "?page=" + String.valueOf(page) + "&resultPerPage=" + elementPerPage)
        .append("\">").append(name).append("</a></li>");
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
