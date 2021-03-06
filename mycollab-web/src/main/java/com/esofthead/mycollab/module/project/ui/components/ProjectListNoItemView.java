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
package com.esofthead.mycollab.module.project.ui.components;

import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public abstract class ProjectListNoItemView extends VerticalLayout implements PageView {
    public ProjectListNoItemView() {
        MVerticalLayout content = new MVerticalLayout().withWidth("700px");
        ELabel image = new ELabel(viewIcon().getHtml(), ContentMode.HTML).withStyleName(ValoTheme.LABEL_H1).withWidthUndefined();
        image.addStyleName(ValoTheme.LABEL_NO_MARGIN);

        ELabel title = ELabel.h2(viewTitle()).withWidthUndefined();

        ELabel body = new ELabel(viewHint(), ContentMode.HTML).withWidthUndefined();
        body.addStyleName(UIConstants.LABEL_WORD_WRAP);
        MHorizontalLayout links = createControlButtons();
        content.with(image, title, body, links).alignAll(Alignment.TOP_CENTER);
        this.addComponent(content);
        this.setComponentAlignment(content, Alignment.MIDDLE_CENTER);
    }

    protected MHorizontalLayout createControlButtons() {
        Button createItemBtn = new Button(actionMessage(), actionListener());
        createItemBtn.setEnabled(hasPermission());
        createItemBtn.addStyleName(UIConstants.BUTTON_ACTION);

        return new MHorizontalLayout().with(createItemBtn);
    }

    abstract protected FontAwesome viewIcon();

    abstract protected String viewTitle();

    abstract protected String viewHint();

    abstract protected String actionMessage();

    abstract protected Button.ClickListener actionListener();

    abstract protected boolean hasPermission();

    @Override
    public ComponentContainer getWidget() {
        return this;
    }

    @Override
    public <E> void addViewListener(ViewListener<E> listener) {

    }
}