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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import com.esofthead.mycollab.module.project.view.ProjectBreadcrumb;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class ProjectSettingPresenter extends AbstractPresenter<ProjectSettingView> {
    private static final long serialVersionUID = 1L;

    public ProjectSettingPresenter() {
        super(ProjectSettingView.class);
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        UserSettingView userSettingView = (UserSettingView) container;
        userSettingView.gotoSubView(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_SETTINGS));

        ProjectNotificationSettingService projectNotificationSettingService = ApplicationContextUtil
                .getSpringBean(ProjectNotificationSettingService.class);
        ProjectNotificationSetting notification = projectNotificationSettingService
                .findNotification(AppContext.getUsername(), CurrentProjectVariables.getProjectId(),
                        AppContext.getAccountId());

        ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
        breadCrumb.gotoProjectSetting();
        view.showNotificationSettings(notification);
    }
}
