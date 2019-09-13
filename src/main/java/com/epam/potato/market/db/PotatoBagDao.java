package com.epam.potato.market.db;

import com.epam.potato.market.api.PotatoBag;
import lombok.RequiredArgsConstructor;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PotatoBagDao {

    private final Nitrite database;

    public List<PotatoBag> fetch(int size) {
        final Cursor<PotatoBag> cursor = getPotatoBagRepository().find(FindOptions.limit(0, size));
        final List<PotatoBag> result = new ArrayList<>();
        for (PotatoBag potatoBag : cursor) {
            result.add(potatoBag);
        }
        return result;
    }

    public int add(PotatoBag potatoBag) {
        return getPotatoBagRepository().insert(potatoBag).getAffectedCount();
    }

    private ObjectRepository<PotatoBag> getPotatoBagRepository() {
        return database.getRepository(PotatoBag.class);
    }
}
