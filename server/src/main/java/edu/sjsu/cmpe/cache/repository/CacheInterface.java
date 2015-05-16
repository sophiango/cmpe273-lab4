package edu.sjsu.cmpe.cache.repository;

import edu.sjsu.cmpe.cache.domain.Entry;

import java.util.List;

/**
 * Entry repository interface.
 * 
 * What is repository pattern?
 * 
 * @see http://martinfowler.com/eaaCatalog/repository.html
 */
public interface CacheInterface {
    /**
     * Save a new entry in the repository
     * 
     * @param newentry
     *            a entry instance to be create in the repository
     * @return an entry instance
     */
    Entry save(Entry newEntry);

    /**
     * Retrieve an existing entry by key
     * 
     * @param key
     *            a valid key
     * @return a entry instance
     */
    Entry get(Long key);

    /**
     * Retrieve all entries
     * 
     * @return a list of entries
     */
    
    Entry delete(Long key);

    /**
     * Delete Key
     * 
     * @return a entry
     */
    List<Entry> getAll();

}
