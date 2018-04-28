package com.ecochain.wallet.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserWalletExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserWalletExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andBtcAmntIsNull() {
            addCriterion("btc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andBtcAmntIsNotNull() {
            addCriterion("btc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andBtcAmntEqualTo(BigDecimal value) {
            addCriterion("btc_amnt =", value, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntNotEqualTo(BigDecimal value) {
            addCriterion("btc_amnt <>", value, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntGreaterThan(BigDecimal value) {
            addCriterion("btc_amnt >", value, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("btc_amnt >=", value, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntLessThan(BigDecimal value) {
            addCriterion("btc_amnt <", value, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("btc_amnt <=", value, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntIn(List<BigDecimal> values) {
            addCriterion("btc_amnt in", values, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntNotIn(List<BigDecimal> values) {
            addCriterion("btc_amnt not in", values, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("btc_amnt between", value1, value2, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andBtcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("btc_amnt not between", value1, value2, "btcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntIsNull() {
            addCriterion("ltc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andLtcAmntIsNotNull() {
            addCriterion("ltc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andLtcAmntEqualTo(BigDecimal value) {
            addCriterion("ltc_amnt =", value, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntNotEqualTo(BigDecimal value) {
            addCriterion("ltc_amnt <>", value, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntGreaterThan(BigDecimal value) {
            addCriterion("ltc_amnt >", value, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ltc_amnt >=", value, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntLessThan(BigDecimal value) {
            addCriterion("ltc_amnt <", value, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ltc_amnt <=", value, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntIn(List<BigDecimal> values) {
            addCriterion("ltc_amnt in", values, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntNotIn(List<BigDecimal> values) {
            addCriterion("ltc_amnt not in", values, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ltc_amnt between", value1, value2, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andLtcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ltc_amnt not between", value1, value2, "ltcAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntIsNull() {
            addCriterion("eth_amnt is null");
            return (Criteria) this;
        }

        public Criteria andEthAmntIsNotNull() {
            addCriterion("eth_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andEthAmntEqualTo(BigDecimal value) {
            addCriterion("eth_amnt =", value, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntNotEqualTo(BigDecimal value) {
            addCriterion("eth_amnt <>", value, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntGreaterThan(BigDecimal value) {
            addCriterion("eth_amnt >", value, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("eth_amnt >=", value, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntLessThan(BigDecimal value) {
            addCriterion("eth_amnt <", value, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("eth_amnt <=", value, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntIn(List<BigDecimal> values) {
            addCriterion("eth_amnt in", values, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntNotIn(List<BigDecimal> values) {
            addCriterion("eth_amnt not in", values, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eth_amnt between", value1, value2, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEthAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("eth_amnt not between", value1, value2, "ethAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntIsNull() {
            addCriterion("etc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andEtcAmntIsNotNull() {
            addCriterion("etc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andEtcAmntEqualTo(BigDecimal value) {
            addCriterion("etc_amnt =", value, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntNotEqualTo(BigDecimal value) {
            addCriterion("etc_amnt <>", value, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntGreaterThan(BigDecimal value) {
            addCriterion("etc_amnt >", value, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("etc_amnt >=", value, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntLessThan(BigDecimal value) {
            addCriterion("etc_amnt <", value, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("etc_amnt <=", value, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntIn(List<BigDecimal> values) {
            addCriterion("etc_amnt in", values, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntNotIn(List<BigDecimal> values) {
            addCriterion("etc_amnt not in", values, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("etc_amnt between", value1, value2, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andEtcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("etc_amnt not between", value1, value2, "etcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntIsNull() {
            addCriterion("hlc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andHlcAmntIsNotNull() {
            addCriterion("hlc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andHlcAmntEqualTo(BigDecimal value) {
            addCriterion("hlc_amnt =", value, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntNotEqualTo(BigDecimal value) {
            addCriterion("hlc_amnt <>", value, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntGreaterThan(BigDecimal value) {
            addCriterion("hlc_amnt >", value, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("hlc_amnt >=", value, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntLessThan(BigDecimal value) {
            addCriterion("hlc_amnt <", value, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("hlc_amnt <=", value, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntIn(List<BigDecimal> values) {
            addCriterion("hlc_amnt in", values, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntNotIn(List<BigDecimal> values) {
            addCriterion("hlc_amnt not in", values, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hlc_amnt between", value1, value2, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andHlcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("hlc_amnt not between", value1, value2, "hlcAmnt");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(BigDecimal value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(BigDecimal value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(BigDecimal value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(BigDecimal value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<BigDecimal> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<BigDecimal> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andLastCaldateIsNull() {
            addCriterion("last_caldate is null");
            return (Criteria) this;
        }

        public Criteria andLastCaldateIsNotNull() {
            addCriterion("last_caldate is not null");
            return (Criteria) this;
        }

        public Criteria andLastCaldateEqualTo(Date value) {
            addCriterion("last_caldate =", value, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateNotEqualTo(Date value) {
            addCriterion("last_caldate <>", value, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateGreaterThan(Date value) {
            addCriterion("last_caldate >", value, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateGreaterThanOrEqualTo(Date value) {
            addCriterion("last_caldate >=", value, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateLessThan(Date value) {
            addCriterion("last_caldate <", value, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateLessThanOrEqualTo(Date value) {
            addCriterion("last_caldate <=", value, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateIn(List<Date> values) {
            addCriterion("last_caldate in", values, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateNotIn(List<Date> values) {
            addCriterion("last_caldate not in", values, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateBetween(Date value1, Date value2) {
            addCriterion("last_caldate between", value1, value2, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andLastCaldateNotBetween(Date value1, Date value2) {
            addCriterion("last_caldate not between", value1, value2, "lastCaldate");
            return (Criteria) this;
        }

        public Criteria andSyscodeIsNull() {
            addCriterion("syscode is null");
            return (Criteria) this;
        }

        public Criteria andSyscodeIsNotNull() {
            addCriterion("syscode is not null");
            return (Criteria) this;
        }

        public Criteria andSyscodeEqualTo(String value) {
            addCriterion("syscode =", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeNotEqualTo(String value) {
            addCriterion("syscode <>", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeGreaterThan(String value) {
            addCriterion("syscode >", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeGreaterThanOrEqualTo(String value) {
            addCriterion("syscode >=", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeLessThan(String value) {
            addCriterion("syscode <", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeLessThanOrEqualTo(String value) {
            addCriterion("syscode <=", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeLike(String value) {
            addCriterion("syscode like", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeNotLike(String value) {
            addCriterion("syscode not like", value, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeIn(List<String> values) {
            addCriterion("syscode in", values, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeNotIn(List<String> values) {
            addCriterion("syscode not in", values, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeBetween(String value1, String value2) {
            addCriterion("syscode between", value1, value2, "syscode");
            return (Criteria) this;
        }

        public Criteria andSyscodeNotBetween(String value1, String value2) {
            addCriterion("syscode not between", value1, value2, "syscode");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntIsNull() {
            addCriterion("froze_rmb_amnt is null");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntIsNotNull() {
            addCriterion("froze_rmb_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntEqualTo(BigDecimal value) {
            addCriterion("froze_rmb_amnt =", value, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntNotEqualTo(BigDecimal value) {
            addCriterion("froze_rmb_amnt <>", value, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntGreaterThan(BigDecimal value) {
            addCriterion("froze_rmb_amnt >", value, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_rmb_amnt >=", value, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntLessThan(BigDecimal value) {
            addCriterion("froze_rmb_amnt <", value, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_rmb_amnt <=", value, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntIn(List<BigDecimal> values) {
            addCriterion("froze_rmb_amnt in", values, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntNotIn(List<BigDecimal> values) {
            addCriterion("froze_rmb_amnt not in", values, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_rmb_amnt between", value1, value2, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeRmbAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_rmb_amnt not between", value1, value2, "frozeRmbAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntIsNull() {
            addCriterion("froze_hlc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntIsNotNull() {
            addCriterion("froze_hlc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntEqualTo(BigDecimal value) {
            addCriterion("froze_hlc_amnt =", value, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntNotEqualTo(BigDecimal value) {
            addCriterion("froze_hlc_amnt <>", value, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntGreaterThan(BigDecimal value) {
            addCriterion("froze_hlc_amnt >", value, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_hlc_amnt >=", value, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntLessThan(BigDecimal value) {
            addCriterion("froze_hlc_amnt <", value, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_hlc_amnt <=", value, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntIn(List<BigDecimal> values) {
            addCriterion("froze_hlc_amnt in", values, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntNotIn(List<BigDecimal> values) {
            addCriterion("froze_hlc_amnt not in", values, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_hlc_amnt between", value1, value2, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeHlcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_hlc_amnt not between", value1, value2, "frozeHlcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntIsNull() {
            addCriterion("froze_btc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntIsNotNull() {
            addCriterion("froze_btc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntEqualTo(BigDecimal value) {
            addCriterion("froze_btc_amnt =", value, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntNotEqualTo(BigDecimal value) {
            addCriterion("froze_btc_amnt <>", value, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntGreaterThan(BigDecimal value) {
            addCriterion("froze_btc_amnt >", value, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_btc_amnt >=", value, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntLessThan(BigDecimal value) {
            addCriterion("froze_btc_amnt <", value, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_btc_amnt <=", value, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntIn(List<BigDecimal> values) {
            addCriterion("froze_btc_amnt in", values, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntNotIn(List<BigDecimal> values) {
            addCriterion("froze_btc_amnt not in", values, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_btc_amnt between", value1, value2, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeBtcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_btc_amnt not between", value1, value2, "frozeBtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntIsNull() {
            addCriterion("froze_ltc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntIsNotNull() {
            addCriterion("froze_ltc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntEqualTo(BigDecimal value) {
            addCriterion("froze_ltc_amnt =", value, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntNotEqualTo(BigDecimal value) {
            addCriterion("froze_ltc_amnt <>", value, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntGreaterThan(BigDecimal value) {
            addCriterion("froze_ltc_amnt >", value, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_ltc_amnt >=", value, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntLessThan(BigDecimal value) {
            addCriterion("froze_ltc_amnt <", value, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_ltc_amnt <=", value, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntIn(List<BigDecimal> values) {
            addCriterion("froze_ltc_amnt in", values, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntNotIn(List<BigDecimal> values) {
            addCriterion("froze_ltc_amnt not in", values, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_ltc_amnt between", value1, value2, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeLtcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_ltc_amnt not between", value1, value2, "frozeLtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntIsNull() {
            addCriterion("froze_eth_amnt is null");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntIsNotNull() {
            addCriterion("froze_eth_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntEqualTo(BigDecimal value) {
            addCriterion("froze_eth_amnt =", value, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntNotEqualTo(BigDecimal value) {
            addCriterion("froze_eth_amnt <>", value, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntGreaterThan(BigDecimal value) {
            addCriterion("froze_eth_amnt >", value, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_eth_amnt >=", value, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntLessThan(BigDecimal value) {
            addCriterion("froze_eth_amnt <", value, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_eth_amnt <=", value, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntIn(List<BigDecimal> values) {
            addCriterion("froze_eth_amnt in", values, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntNotIn(List<BigDecimal> values) {
            addCriterion("froze_eth_amnt not in", values, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_eth_amnt between", value1, value2, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEthAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_eth_amnt not between", value1, value2, "frozeEthAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntIsNull() {
            addCriterion("froze_etc_amnt is null");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntIsNotNull() {
            addCriterion("froze_etc_amnt is not null");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntEqualTo(BigDecimal value) {
            addCriterion("froze_etc_amnt =", value, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntNotEqualTo(BigDecimal value) {
            addCriterion("froze_etc_amnt <>", value, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntGreaterThan(BigDecimal value) {
            addCriterion("froze_etc_amnt >", value, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_etc_amnt >=", value, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntLessThan(BigDecimal value) {
            addCriterion("froze_etc_amnt <", value, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntLessThanOrEqualTo(BigDecimal value) {
            addCriterion("froze_etc_amnt <=", value, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntIn(List<BigDecimal> values) {
            addCriterion("froze_etc_amnt in", values, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntNotIn(List<BigDecimal> values) {
            addCriterion("froze_etc_amnt not in", values, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_etc_amnt between", value1, value2, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andFrozeEtcAmntNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("froze_etc_amnt not between", value1, value2, "frozeEtcAmnt");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andOperatorIsNull() {
            addCriterion("operator is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIsNotNull() {
            addCriterion("operator is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorEqualTo(String value) {
            addCriterion("operator =", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotEqualTo(String value) {
            addCriterion("operator <>", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorGreaterThan(String value) {
            addCriterion("operator >", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorGreaterThanOrEqualTo(String value) {
            addCriterion("operator >=", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLessThan(String value) {
            addCriterion("operator <", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLessThanOrEqualTo(String value) {
            addCriterion("operator <=", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorLike(String value) {
            addCriterion("operator like", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotLike(String value) {
            addCriterion("operator not like", value, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorIn(List<String> values) {
            addCriterion("operator in", values, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotIn(List<String> values) {
            addCriterion("operator not in", values, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorBetween(String value1, String value2) {
            addCriterion("operator between", value1, value2, "operator");
            return (Criteria) this;
        }

        public Criteria andOperatorNotBetween(String value1, String value2) {
            addCriterion("operator not between", value1, value2, "operator");
            return (Criteria) this;
        }

        public Criteria andVersionIdIsNull() {
            addCriterion("version_id is null");
            return (Criteria) this;
        }

        public Criteria andVersionIdIsNotNull() {
            addCriterion("version_id is not null");
            return (Criteria) this;
        }

        public Criteria andVersionIdEqualTo(String value) {
            addCriterion("version_id =", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotEqualTo(String value) {
            addCriterion("version_id <>", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdGreaterThan(String value) {
            addCriterion("version_id >", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdGreaterThanOrEqualTo(String value) {
            addCriterion("version_id >=", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdLessThan(String value) {
            addCriterion("version_id <", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdLessThanOrEqualTo(String value) {
            addCriterion("version_id <=", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdLike(String value) {
            addCriterion("version_id like", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotLike(String value) {
            addCriterion("version_id not like", value, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdIn(List<String> values) {
            addCriterion("version_id in", values, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotIn(List<String> values) {
            addCriterion("version_id not in", values, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdBetween(String value1, String value2) {
            addCriterion("version_id between", value1, value2, "versionId");
            return (Criteria) this;
        }

        public Criteria andVersionIdNotBetween(String value1, String value2) {
            addCriterion("version_id not between", value1, value2, "versionId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}