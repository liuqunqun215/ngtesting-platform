package com.ngtesting.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.ngtesting.platform.entity.TestOrgPrivilege;
import com.ngtesting.platform.entity.TestOrgRole;
import com.ngtesting.platform.service.OrgRolePrivilegeService;
import com.ngtesting.platform.vo.OrgPrivilegeVo;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrgRolePrivilegeServiceImpl extends BaseServiceImpl implements OrgRolePrivilegeService {

	@Override
	public List<OrgPrivilegeVo> listPrivilegesByOrgRole(Long orgId, Long orgRoleId) {

        List<TestOrgPrivilege> allPrivileges = listAllOrgPrivileges();

        List<TestOrgPrivilege> orgRolePrivileges;
        if (orgRoleId == null) {
        	orgRolePrivileges = new LinkedList<>();
        } else {
        	orgRolePrivileges = listOrgRolePrivileges(orgId, orgRoleId);
        }

        List<OrgPrivilegeVo> vos = new LinkedList<OrgPrivilegeVo>();
        for (TestOrgPrivilege po1 : allPrivileges) {
        	OrgPrivilegeVo vo = genVo(orgId, po1);

        	vo.setSelected(false);
        	vo.setSelecting(false);
        	for (TestOrgPrivilege po2 : orgRolePrivileges) {
        		if (po1.getId().longValue() == po2.getId().longValue()) {
            		vo.setSelected(true);
            		vo.setSelecting(true);
            	}
        	}
        	vos.add(vo);
        }

		return vos;
	}

	private OrgPrivilegeVo genVo(Long orgId, TestOrgPrivilege po1) {
		OrgPrivilegeVo vo = new OrgPrivilegeVo(po1.getId(), po1.getName(), po1.getDescr(), orgId);

		return vo;
	}

	private List<TestOrgPrivilege> listOrgRolePrivileges(Long orgId, Long orgRoleId) {

		DetachedCriteria dc = DetachedCriteria.forClass(TestOrgPrivilege.class);

        dc.createAlias("orgRoleSet", "roles");
        dc.add(Restrictions.eq("roles.id", orgRoleId));

        dc.add(Restrictions.eq("disabled", Boolean.FALSE));
        dc.add(Restrictions.eq("deleted", Boolean.FALSE));

        dc.addOrder(Order.asc("id"));
        List ls = findAllByCriteria(dc);

		return ls;
	}

	private List<TestOrgPrivilege> listAllOrgPrivileges() {
		DetachedCriteria dc = DetachedCriteria.forClass(TestOrgPrivilege.class);

        dc.add(Restrictions.eq("deleted", Boolean.FALSE));
        dc.add(Restrictions.eq("disabled", Boolean.FALSE));
        dc.addOrder(Order.asc("id"));
        List<TestOrgPrivilege> ls = findAllByCriteria(dc);

		return ls;
	}

	@Override
	public boolean saveOrgRolePrivileges(Long roleId, List<OrgPrivilegeVo> orgPrivileges) {
		if (orgPrivileges == null) {
			return false;
		}

		TestOrgRole orgRole = (TestOrgRole) get(TestOrgRole.class, roleId);
		Set<TestOrgPrivilege> privilegeSet = orgRole.getOrgPrivilegeSet();

		for (Object obj: orgPrivileges) {
			OrgPrivilegeVo vo = JSON.parseObject(JSON.toJSONString(obj), OrgPrivilegeVo.class);
			if (vo.getSelecting() != vo.getSelected()) { // 变化了
				TestOrgPrivilege orgPrivilege = (TestOrgPrivilege) get(TestOrgPrivilege.class, vo.getId());

    			if (vo.getSelecting() && !privilegeSet.contains(orgPrivilege)) { // 勾选
    				privilegeSet.add(orgPrivilege);
    			} else if (orgPrivilege != null) { // 取消
    				privilegeSet.remove(orgPrivilege);
    			}
			}
		}
		saveOrUpdate(orgRole);

		return true;
	}

	@Override
	public Map<String, Boolean> listByUser(Long userId, Long orgId) {
	    String hql = "select role from TestOrgRole role" +
                " join role.userSet users " +
                " where users.id = ?" +
                " and role.orgId = ?" +

                " and role.deleted != true and role.disabled!= true " +
                " order by role.id asc";

		List<TestOrgRole> ls = getDao().getListByHQL(hql, userId, orgId);

		Map<String, Boolean> map = new HashMap();
		for (TestOrgRole role: ls) {
            for (TestOrgPrivilege priv: role.getOrgPrivilegeSet()) {
                map.put(priv.getCode().toString(), true);
            }
		}

		return map;
	}

}
