/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public final class BugFieldFormatter extends FieldGroupFormatter {
    private static BugFieldFormatter _instance = new BugFieldFormatter();

    private BugFieldFormatter() {
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION);
        generateFieldDisplayHandler("environment", BugI18nEnum.FORM_ENVIRONMENT);
        generateFieldDisplayHandler("summary", BugI18nEnum.FORM_SUMMARY);
        generateFieldDisplayHandler("status", BugI18nEnum.FORM_STATUS,
                new I18nHistoryFieldFormat(OptionI18nEnum.BugStatus.class));
        generateFieldDisplayHandler("priority", BugI18nEnum.FORM_PRIORITY,
                new I18nHistoryFieldFormat(OptionI18nEnum.BugPriority.class));
        generateFieldDisplayHandler("severity", BugI18nEnum.FORM_SEVERITY,
                new I18nHistoryFieldFormat(OptionI18nEnum.BugSeverity.class));
        generateFieldDisplayHandler("resolution", BugI18nEnum.FORM_RESOLUTION,
                new I18nHistoryFieldFormat(OptionI18nEnum.BugResolution.class));
        generateFieldDisplayHandler("estimateremaintime", BugI18nEnum.FORM_REMAIN_ESTIMATE);
        generateFieldDisplayHandler("duedate", BugI18nEnum.FORM_DUE_DATE, FieldGroupFormatter.DATE_FIELD);
        generateFieldDisplayHandler("createdTime", BugI18nEnum.FORM_CREATED_TIME, FieldGroupFormatter.PRETTY_DATE_TIME_FIELD);
        generateFieldDisplayHandler("loguserFullName",
                BugI18nEnum.FORM_LOG_BY, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, new ProjectMemberHistoryFieldFormat());
        generateFieldDisplayHandler("milestoneid", TaskI18nEnum.FORM_PHASE, new MilestoneHistoryFieldFormat());
    }

    public static BugFieldFormatter instance() {
        return _instance;
    }
}
