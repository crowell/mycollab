/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.web.ui;

import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class AddViewLayout extends CustomLayoutExt {
    private static final long serialVersionUID = 1L;

    private Resource viewIcon;
    private Label titleLbl;
    private final MHorizontalLayout header;

    public AddViewLayout(String viewTitle, Resource viewIcon) {
        super("addView");

        this.viewIcon = viewIcon;

        header = new MHorizontalLayout().withWidth("100%").withMargin(new MarginInfo(true, true, true, false));
        header.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        titleLbl = ELabel.h3("");
        titleLbl.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        if (!(viewIcon instanceof FontAwesome)) {
            Image icon = new Image(null);
            icon.setIcon(viewIcon);
            icon.addStyleName(UIConstants.BUTTON_ICON_ONLY);
            this.header.with(icon);
        }
        header.with(titleLbl).expand(titleLbl);
        setHeader(viewTitle);
        addComponent(header, "addViewHeader");
    }

    public void addBody(final ComponentContainer body) {
        addComponent(body, "addViewBody");
    }

    public void addBottomControls(final ComponentContainer bottomControls) {
        this.addComponent(bottomControls, "addViewBottomControls");
    }

    public void addHeaderRight(final ComponentContainer headerRight) {
        header.addComponent(headerRight);
    }

    public void addTitleStyleName(final String styleName) {
        titleLbl.addStyleName(styleName);
    }

    public void setHeader(final String viewTitle) {
        if (viewIcon instanceof FontAwesome) {
            String title = ((FontAwesome) viewIcon).getHtml() + " " + viewTitle;
            titleLbl.setValue(title);
        } else {
            titleLbl.setValue(viewTitle);
        }
    }

    public void setTitle(final String title) {
        if (title != null) {
            CssLayout titleWrap = new CssLayout();
            titleWrap.setWidth("100%");
            titleWrap.addComponent(ELabel.h2(title));
            addComponent(titleWrap, "addViewTitle");
        } else {
            removeComponent("addViewTitle");
        }
    }
}
