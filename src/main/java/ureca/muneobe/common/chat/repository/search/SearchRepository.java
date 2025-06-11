package ureca.muneobe.common.chat.repository.search;

import ureca.muneobe.common.chat.service.rdb.input.Condition;

import java.util.List;

public interface SearchRepository<T> {
    List<T> search(final Condition condition);
}
