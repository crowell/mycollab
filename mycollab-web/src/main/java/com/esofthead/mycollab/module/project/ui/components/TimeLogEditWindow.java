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

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.ValuedBean;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.module.project.view.time.TimeTableFieldDef;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DateFieldExt;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table.ColumnGenerator;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.3.3
 */
public abstract class TimeLogEditWindow<V extends ValuedBean> extends Window {
    private static final long serialVersionUID = 1L;

    protected ItemTimeLoggingService itemTimeLoggingService;
    protected V bean;

    private DefaultPagedBeanTable<ItemTimeLoggingService, ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging> tableItem;

    private MVerticalLayout content;
    private HorizontalLayout headerPanel;

    private boolean hasTimeChange = false;
    private Button addBtn;
    private Label totalSpentTimeLbl;
    private NumericTextField newTimeInputField;
    private CheckBox isBillableField;
    private DateFieldExt forDateField;

    private NumericTextField remainTimeInputField;
    private Label remainTimeLbl;

    protected TimeLogEditWindow(final V bean) {
        this.bean = bean;
        this.center();
        this.setResizable(false);
        this.setModal(true);
        content = new MVerticalLayout();
        this.setContent(content);

        this.itemTimeLoggingService = ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class);

        this.initUI();
        this.loadTimeValue();
        this.addCloseListener(new CloseListener() {
            @Override
            public void windowClose(CloseEvent e) {
                if (hasTimeChange) {
                    EventBusFactory.getInstance().post(new ProjectEvent.TimeLoggingChangedEvent(TimeLogEditWindow.this));
                }
            }
        });
    }

    private void initUI() {
        this.setWidth("900px");

        headerPanel = new MHorizontalLayout().withWidth("100%");
        content.addComponent(headerPanel);
        constructSpentTimeEntryPanel();
        constructRemainTimeEntryPanel();

        tableItem = new DefaultPagedBeanTable<>(ApplicationContextUtil.getSpringBean(ItemTimeLoggingService.class), SimpleItemTimeLogging.class,
                Arrays.asList(TimeTableFieldDef.logUser(), TimeTableFieldDef.logForDate(), TimeTableFieldDef.logValue(),
                        TimeTableFieldDef.billable(), new TableViewField(null, "id", UIConstants.TABLE_CONTROL_WIDTH)));

        tableItem.addGeneratedColumn("logUserFullName", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(final Table source, final Object itemId, final Object columnId) {
                final SimpleItemTimeLogging timeLoggingItem = tableItem.getBeanByIndex(itemId);

                return new ProjectUserLink(timeLoggingItem.getLoguser(), timeLoggingItem.getLogUserAvatarId(),
                        timeLoggingItem.getLogUserFullName());

            }
        });

        tableItem.addGeneratedColumn("logforday", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        Object itemId, Object columnId) {
                SimpleItemTimeLogging monitorItem = tableItem.getBeanByIndex(itemId);
                return new Label(AppContext.formatDate(monitorItem.getLogforday()));
            }
        });

        tableItem.addGeneratedColumn("logvalue", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        Object itemId, Object columnId) {
                SimpleItemTimeLogging itemTimeLogging = tableItem.getBeanByIndex(itemId);
                return new Label(itemTimeLogging.getLogvalue() + "");
            }
        });

        tableItem.addGeneratedColumn("isbillable", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {
                SimpleItemTimeLogging monitorItem = tableItem.getBeanByIndex(itemId);
                ELabel icon = (monitorItem.getIsbillable()) ? new ELabel(FontAwesome.CHECK) : new ELabel(FontAwesome.TIMES);
                icon.setStyleName(UIConstants.BUTTON_ICON_ONLY);
                return icon;
            }
        });

        tableItem.addGeneratedColumn("id", new ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source,
                                                        Object itemId, Object columnId) {
                final SimpleItemTimeLogging itemTimeLogging = tableItem.getBeanByIndex(itemId);
                Button deleteBtn = new Button(null, new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        itemTimeLoggingService.removeWithSession(itemTimeLogging, AppContext.getUsername(), AppContext.getAccountId());
                        TimeLogEditWindow.this.loadTimeValue();
                        hasTimeChange = true;
                    }
                });
                deleteBtn.setIcon(FontAwesome.TRASH_O);
                deleteBtn.addStyleName(UIConstants.BUTTON_ICON_ONLY);
                itemTimeLogging.setExtraData(deleteBtn);

                deleteBtn.setEnabled(CurrentProjectVariables.isAdmin() || AppContext.getUsername().equals(itemTimeLogging.getLoguser()));
                return deleteBtn;
            }
        });

        tableItem.setWidth("100%");
        content.addComponent(tableItem);
    }

    private void constructSpentTimeEntryPanel() {
        VerticalLayout spentTimePanel = new VerticalLayout();
        spentTimePanel.setSpacing(true);
        headerPanel.addComponent(spentTimePanel);

        final VerticalLayout totalLayout = new VerticalLayout();
        totalLayout.setMargin(true);
        totalLayout.addStyleName("boxTotal");
        totalLayout.setWidth("100%");
        spentTimePanel.addComponent(totalLayout);
        Label lbTimeInstructTotal = new Label(AppContext.getMessage(TimeTrackingI18nEnum.OPT_TOTAL_SPENT_HOURS));
        totalLayout.addComponent(lbTimeInstructTotal);
        totalSpentTimeLbl = new Label("_");
        totalSpentTimeLbl.addStyleName("numberTotal");
        totalLayout.addComponent(totalSpentTimeLbl);

        MHorizontalLayout addLayout = new MHorizontalLayout();
        addLayout.setSizeUndefined();
        addLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        spentTimePanel.addComponent(addLayout);

        newTimeInputField = new NumericTextField();
        newTimeInputField.setWidth("80px");

        forDateField = new DateFieldExt();
        forDateField.setValue(new GregorianCalendar().getTime());

        isBillableField = new CheckBox(AppContext.getMessage(TimeTrackingI18nEnum.FORM_IS_BILLABLE), true);

        addBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADD), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                double d = 0;
                try {
                    d = Double.parseDouble(newTimeInputField.getValue());
                } catch (NumberFormatException e) {
                    NotificationUtil.showWarningNotification("You must enter a positive number value");
                }
                if (d > 0) {
                    hasTimeChange = true;
                    saveTimeInvest();
                    loadTimeValue();
                    newTimeInputField.setValue("0.0");
                }
            }

        });

        addBtn.setEnabled(isEnableAdd());
        addBtn.setStyleName(UIConstants.BUTTON_ACTION);
        addBtn.setIcon(FontAwesome.PLUS);
        addLayout.with(newTimeInputField, forDateField, isBillableField, addBtn);
    }

    private void constructRemainTimeEntryPanel() {
        VerticalLayout remainTimePanel = new VerticalLayout();
        remainTimePanel.setSpacing(true);
        headerPanel.addComponent(remainTimePanel);

        final VerticalLayout updateLayout = new VerticalLayout();
        updateLayout.setMargin(true);
        updateLayout.addStyleName("boxTotal");
        updateLayout.setWidth("100%");
        remainTimePanel.addComponent(updateLayout);

        final Label lbTimeInstructTotal = new Label(AppContext.getMessage(TimeTrackingI18nEnum.OPT_REMAINING_WORK_HOURS));
        updateLayout.addComponent(lbTimeInstructTotal);
        remainTimeLbl = new Label("_");
        remainTimeLbl.addStyleName("numberTotal");
        updateLayout.addComponent(remainTimeLbl);

        MHorizontalLayout addLayout = new MHorizontalLayout();
        addLayout.setSizeUndefined();
        remainTimePanel.addComponent(addLayout);

        remainTimeInputField = new NumericTextField();
        remainTimeInputField.setWidth("80px");
        addLayout.addComponent(remainTimeInputField);
        addLayout.setComponentAlignment(remainTimeInputField, Alignment.MIDDLE_LEFT);

        addBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                try {
                    double d = 0;
                    try {
                        d = Double.parseDouble(remainTimeInputField.getValue());
                    } catch (Exception e) {
                        NotificationUtil.showWarningNotification("You must enter a positive number value");
                    }
                    if (d >= 0) {
                        hasTimeChange = true;
                        updateTimeRemain();
                        remainTimeLbl.setValue(remainTimeInputField.getValue());
                        remainTimeInputField.setValue("0.0");
                    }
                } catch (final Exception e) {
                    remainTimeInputField.setValue("0.0");
                }
            }
        });

        addBtn.setEnabled(isEnableAdd());
        addBtn.setStyleName(UIConstants.BUTTON_ACTION);
        addLayout.with(addBtn).withAlign(addBtn, Alignment.MIDDLE_LEFT);
    }

    public void loadTimeValue() {
        ItemTimeLoggingSearchCriteria searchCriteria = getItemSearchCriteria();
        tableItem.setSearchCriteria(searchCriteria);
        setTotalTimeValue();
        setUpdateTimeValue();
    }

    @SuppressWarnings("unchecked")
    private double getTotalInvest() {
        double total = 0;
        ItemTimeLoggingSearchCriteria searchCriteria = getItemSearchCriteria();
        List<SimpleItemTimeLogging> listTime = itemTimeLoggingService.findPagableListByCriteria(
                new SearchRequest<>(searchCriteria, 0, Integer.MAX_VALUE));
        for (SimpleItemTimeLogging simpleItemTimeLogging : listTime) {
            total += simpleItemTimeLogging.getLogvalue();
        }
        return total;
    }

    private void setUpdateTimeValue() {
        if (getEstimateRemainTime() > -1) {
            remainTimeLbl.setValue(getEstimateRemainTime() + "");
        }
    }

    private void setTotalTimeValue() {
        if (getTotalInvest() > 0) {
            totalSpentTimeLbl.setValue(getTotalInvest() + "");
        }
    }

    protected double getInvestValue() {
        return Double.parseDouble(newTimeInputField.getValue());
    }

    protected Boolean isBillableHours() {
        return isBillableField.getValue();
    }

    protected Date forLogDate() {
        Date date = forDateField.getValue();
        return (date != null) ? date : new GregorianCalendar().getTime();
    }

    protected abstract void saveTimeInvest();

    protected abstract void updateTimeRemain();

    protected abstract ItemTimeLoggingSearchCriteria getItemSearchCriteria();

    protected abstract double getEstimateRemainTime();

    protected abstract boolean isEnableAdd();

    protected double getUpdateRemainTime() {
        return Double.parseDouble(remainTimeInputField.getValue());
    }

    private class NumericTextField extends TextField {
        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(String newValue, boolean repaintIsNotNeeded) {
            try {
                String d = Double.parseDouble(newValue) + "";
                super.setValue(d, repaintIsNotNeeded);
            } catch (Exception e) {
                super.setValue("0.0", repaintIsNotNeeded);
            }
        }
    }
}
