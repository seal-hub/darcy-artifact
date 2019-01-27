package org.modules.beer.dao;

import org.modules.base.dao.BaseDao;
import org.modules.beer.domain.Beer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by aleksandra on 05.10.17.
 */
public class BeerDao implements BaseDao<Beer>{

    private long index = 1;
    private Map<Long, Beer> beers = new HashMap<>();

    public BeerDao(){
        beers.put(index, new Beer("Grolsch", 30));
        beers.put(++index, new Beer("Budweiser", 22));
        beers.put(++index, new Beer("Becks", 21));
    }

    @Override
    public Beer getOne(Long id) {
        if(beers.containsKey(id))
            return beers.get(id);
        else
            return null;
    }

    @Override
    public Map<Long, Beer> getAll() {
        return beers;
    }

    @Override
    public void add(Beer obj) {
        if(obj.validate())
            beers.put(++index, obj);
        else
            return;
    }
}
