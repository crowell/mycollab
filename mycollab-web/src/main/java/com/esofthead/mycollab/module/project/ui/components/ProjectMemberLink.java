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

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import java.util.UUID;

/**
 * @author MyCollab Ltd.
 * @version 5.0.4
 */
public class ProjectMemberLink extends Label {
    public ProjectMemberLink(String username, String userAvatarId, String displayName) {
        if (StringUtils.isBlank(username)) {
            return;
        }
        this.setContentMode(ContentMode.HTML);
        String uid = UUID.randomUUID().toString();
        DivLessFormatter div = new DivLessFormatter();
        Img userAvatar = new Img("", StorageFactory.getInstance().getAvatarPath(userAvatarId, 16));
        A userLink = new A().setId("tag" + uid).setHref(ProjectLinkBuilder.generateProjectMemberFullLink(CurrentProjectVariables.getProjectId(), username))
                .appendText(StringUtils.trim(displayName, 30, true));
        userLink.setAttribute("onmouseover", TooltipHelper.userHoverJsFunction(uid, username));
        userLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction(uid));
        div.appendChild(userAvatar, DivLessFormatter.EMPTY_SPACE(), userLink, DivLessFormatter.EMPTY_SPACE(), TooltipHelper.buildDivTooltipEnable(uid));
        this.setValue(div.write());
    }
}
