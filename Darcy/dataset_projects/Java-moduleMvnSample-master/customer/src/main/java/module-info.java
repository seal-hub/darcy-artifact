module org.modules.customer {

    exports org.modules.customer.domain;
    exports org.modules.customer.dao;

    requires org.modules.base;

    provides org.modules.base.dao.BaseDao
            with org.modules.customer.dao.CustomerDao;
}