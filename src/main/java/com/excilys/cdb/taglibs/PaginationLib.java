package com.excilys.cdb.taglibs;

import java.io.Writer;

import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.services.Facade;

/**
 * TagLib qui permet de gérer la pagination.
 * @author martin
 *
 */
public class PaginationLib extends SimpleTagSupport {

    /**
     * la page courante.
     */
    private int currentPage;

    /**
     * le nombre d'élement total.
     */
    private int numberOfElement;

    /**
     * le nombre d'élement par page.
     */
    private int elementPerPage;

    /**
     * L'URI de redirection.
     */
    private String uri;

    /**
     * la rechercher effectuée.
     */
    private String search;

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
            writer.write(currentPage > 1 ? createLink(1, "<<") + createLink(currentPage - 1, "<")
            : "<li class=\"disabled\"><a><<</a></li>" + "<li class=\"disabled\"><a><</a></li>");

            for (int i = startPage; i < endPage; i++) {
                writer.write(i == currentPage ? "<li class=\"active\"><a>" + currentPage + "</a></li>"
                        : createLink(i, String.valueOf(i)));
            }

            writer.write(currentPage < numberOfPage ? createLink(currentPage + 1, ">") + createLink(numberOfPage, ">>")
            : "<li class=\"disabled\"><a>></a></li>" + "<li class=\"disabled\"><a>>></a></li>");
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
        link.append("<a href=\"").append(
                uri + "?search=" + search + "&page=" + String.valueOf(page) + "&resultPerPage=" + elementPerPage)
        .append("\">").append(name).append("</a></li>");
        return link.toString();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
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
