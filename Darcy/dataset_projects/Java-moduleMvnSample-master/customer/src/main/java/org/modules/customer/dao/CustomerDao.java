package org.modules.customer.dao;

import org.modules.base.dao.BaseDao;
import org.modules.customer.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by aleksandra on 05.10.17.
 */
public class CustomerDao implements BaseDao<Customer> {

    private long index = 1;
    private Map<Long, Customer> customers = new HashMap<>();

    @Override
    public Customer getOne(Long id) {
        if(customers.containsKey(id))
            return customers.get(id);
        else
            return null;
    }

    @Override
    public Map<Long, Customer> getAll() {
        return customers;
    }

    @Override
    public void add(Customer obj) {
        if(obj.validate() && obj.isOver18()) {
            System.out.println("Welcome on board!");
            customers.put(++index, obj);
        }
        else
            return;
    }
}
