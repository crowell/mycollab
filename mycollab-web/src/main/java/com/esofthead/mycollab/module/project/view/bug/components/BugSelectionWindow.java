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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.view.bug.BugSimpleSearchPanel;
import com.esofthead.mycollab.module.project.view.bug.BugTableDisplay;
import com.esofthead.mycollab.module.project.view.bug.BugTableFieldDef;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.web.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSelectionWindow extends Window {
    private static final long serialVersionUID = 1L;

    private DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> tableItem;
    private FieldSelection<SimpleBug> fieldSelection;

    public BugSelectionWindow(FieldSelection<SimpleBug> fieldSelection) {
        super("Bug Selection");

        this.setWidth("900px");
        this.setHeight("800px");
        this.setModal(true);
        this.setResizable(false);
        this.fieldSelection = fieldSelection;

        MVerticalLayout layout = new MVerticalLayout();
        BugSimpleSearchPanel contactSimpleSearchPanel = new BugSimpleSearchPanel();
        contactSimpleSearchPanel.addSearchHandler(new SearchHandler<BugSearchCriteria>() {
            @Override
            public void onSearch(BugSearchCriteria criteria) {
                tableItem.setSearchCriteria(criteria);
            }

        });
        layout.addComponent(contactSimpleSearchPanel);
        createBugList();
        layout.addComponent(tableItem);
        this.setContent(layout);
        show();
    }

    public void show() {
        BugSearchCriteria criteria = new BugSearchCriteria();
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        tableItem.setSearchCriteria(criteria);
    }

    private void createBugList() {
        tableItem = new BugTableDisplay(Arrays.asList(BugTableFieldDef.summary(),
                BugTableFieldDef.severity(), BugTableFieldDef.resolution(),
                BugTableFieldDef.assignUser()));

        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("summary", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source, Object itemId, Object columnId) {
                final SimpleBug bug = tableItem.getBeanByIndex(itemId);

                String bugName = "[%s-%s] %s";
                bugName = String.format(bugName, CurrentProjectVariables.getProject().getShortname(),
                        bug.getBugkey(), bug.getSummary());

                ButtonLink b = new ButtonLink(bugName, new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        fieldSelection.fireValueChange(bug);
                        BugSelectionWindow.this.close();
                    }
                });

                if (bug.isCompleted()) {
                    b.addStyleName(UIConstants.LINK_COMPLETED);
                } else if (bug.isOverdue()) {
                    b.addStyleName(UIConstants.LINK_OVERDUE);
                }

                b.setDescription(ProjectTooltipGenerator.generateToolTipBug(AppContext.getUserLocale(), bug, AppContext.getSiteUrl(),
                        AppContext.getUserTimezone(), false));
                return b;

            }
        });

    }
}
