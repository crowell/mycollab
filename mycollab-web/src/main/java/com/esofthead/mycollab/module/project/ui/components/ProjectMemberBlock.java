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

import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @since 5.0.4
 */
public class ProjectMemberBlock extends MVerticalLayout {
    public ProjectMemberBlock(String username, String userAvatarId, String displayName) {
        withMargin(false).withWidth("80px");

        Image userAvatar = UserAvatarControlFactory.createUserAvatarEmbeddedComponent(userAvatarId, 48, displayName);

        String uid = UUID.randomUUID().toString();
        DivLessFormatter div = new DivLessFormatter();
        A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables.getProjectId(),
                username)).appendText(StringUtils.trim(displayName, 30, true));
        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, username));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        div.appendChild(userLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
        Label userLbl = new Label(div.write(), ContentMode.HTML);
        userLbl.addStyleName(ValoTheme.LABEL_SMALL);
        with(userAvatar, userLbl);
    }
}
