package com.epam.potato.db;

import com.epam.potato.api.PotatoBag;
import lombok.RequiredArgsConstructor;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PotatoBagStore {

    private final Nitrite db;

    public List<PotatoBag> fetch(int offset, int size) {
        final Cursor<PotatoBag> cursor = db.getRepository(PotatoBag.class)
                .find(FindOptions.limit(offset, size));
        final List<PotatoBag> result = new ArrayList<>();
        for (PotatoBag potatoBag : cursor) {
            result.add(potatoBag);
        }
        return result;
    }

    public int add(PotatoBag potatoBag) {
        return db.getRepository(PotatoBag.class).insert(potatoBag).getAffectedCount();
    }
}
