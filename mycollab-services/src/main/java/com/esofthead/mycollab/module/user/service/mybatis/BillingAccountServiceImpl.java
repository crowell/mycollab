/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.service.mybatis;

import com.esofthead.mycollab.configuration.IDeploymentMode;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.core.persistence.service.DefaultCrudService;
import com.esofthead.mycollab.module.user.dao.BillingAccountMapper;
import com.esofthead.mycollab.module.user.dao.BillingAccountMapperExt;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.BillingAccountExample;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Service
public class BillingAccountServiceImpl extends DefaultCrudService<Integer, BillingAccount> implements BillingAccountService {

    @Autowired
    private BillingAccountMapper billingAccountMapper;

    @Autowired
    private BillingAccountMapperExt billingAccountMapperExt;

    @Autowired
    private IDeploymentMode deploymentMode;

    @Override
    public ICrudGenericDAO<Integer, BillingAccount> getCrudMapper() {
        return billingAccountMapper;
    }

    @Override
    public SimpleBillingAccount getBillingAccountById(Integer accountId) {
        return billingAccountMapperExt.getBillingAccountById(accountId);
    }

    @Override
    public Integer updateSelectiveWithSession(BillingAccount record, String username) {
        try {
            return super.updateSelectiveWithSession(record, username);
        } catch (DuplicateKeyException e) {
            throw new UserInvalidInputException("The domain " + record.getSubdomain() + " is already used");
        }
    }

    @Override
    public BillingAccount getAccountByDomain(String domain) {
        BillingAccountExample ex = new BillingAccountExample();

        if (deploymentMode.isDemandEdition()) {
            ex.createCriteria().andSubdomainEqualTo(domain);
        }

        List<BillingAccount> accounts = billingAccountMapper.selectByExample(ex);
        if (accounts == null || accounts.size() == 0) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

    @Override
    public BillingAccount getAccountById(Integer accountId) {
        BillingAccountExample ex = new BillingAccountExample();

        if (deploymentMode.isDemandEdition()) {
            ex.createCriteria().andIdEqualTo(accountId);
        }

        List<BillingAccount> accounts = billingAccountMapper.selectByExample(ex);
        if (accounts == null || accounts.size() == 0) {
            return null;
        } else {
            return accounts.get(0);
        }
    }

}
