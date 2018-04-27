package com.excilys.cdb.taglibs;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.services.Facade;

public class LinkLib extends SimpleTagSupport {
    private int page;
    private int count;
    private String uri;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Facade.class);

    /**
     * Affiche les différents éléments de pagination.
     *
     */
    @Override
    public void doTag() {
        JspWriter writer = getJspContext().getOut();
        try {
            writer.print("\"" + uri + "?page=" + page + "&resultPerPage=" + count + "\"");
        } catch (IOException e) {
            LOGGER.debug("LINK LIB ERROR " + e.getMessage());
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
