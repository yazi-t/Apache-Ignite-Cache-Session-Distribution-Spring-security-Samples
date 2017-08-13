package test.prog.cache;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import test.prog.IgniteManger;
import test.prog.sample.Person;

import javax.cache.Cache;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>This application demonstrates usages of apache ignite.</p>
 * <p>Implements sample usages of ignite cache</p>
 *
 * @author Yasitha Thilakaratne
 * @see <a href="https://ignite.apache.org/releases/2.1.0/javadoc/index.html">Apache Ignite</a>
 * @since v-1.0.0
 */
public class IgniteCacheManager extends IgniteManger {

    /**
     * singleton eager loaded instance
     */
    private static IgniteCacheManager INSTANCE = new IgniteCacheManager();

    public static IgniteCacheManager getInstance() {
        return INSTANCE;
    }

    protected static IgniteCache<Long, Person> igniteCache;
    static {
        CacheConfiguration<Long, Person> ccfg = new CacheConfiguration<>(CACHE_NAME);
        ccfg.setIndexedTypes(Long.class, Person.class);
        ccfg.setIndexedTypes(String.class, Person.class);
        igniteCache = ignite.getOrCreateCache(ccfg);
    }

    /**
     * @param key id of person object
     * @return object from cache
     */
    public Person getFromCache(long key) {
        return igniteCache.get(key);
    }

    /**
     * @param key id of person object
     * @param value person object to put to the cache
     */
    public void putToCache(long key, Person value) {
        igniteCache.put(key, value);
    }

    /**
     * @return all available entries from cache
     */
    public List<Person> getAllFromCache() {
        try (QueryCursor<Cache.Entry<Long, Person>> cursor = igniteCache.query(new ScanQuery<Long, Person>())) {
            List<Person> persons = new ArrayList<>();
            for (Cache.Entry<Long, Person> entry: cursor) {
                persons.add(entry.getValue());
            }
            return persons;
        }
    }

    /**
     * @param key id of person object
     * @param cacheName cache name
     * @return stored data in caches in format of BinaryObjects.
     */
    public BinaryObject getBinaryObjecr(long key, String cacheName) {
        IgniteCache<Long, BinaryObject> binaryCache = ignite.cache(cacheName).withKeepBinary();
        return binaryCache.get(key);
    }

    /**
     * Modifies binary object. BinaryObject instances are immutable.
     * An instance of BinaryObjectBuildermust be used in order to update fields and create a new BinaryObject.
     *
     * @param key key to object
     * @param cacheName cache name
     * @param field field to update
     * @param value new value
     */
    public void modifyObject(long key, String cacheName, final String field, final Object value) {
        IgniteCache<Long, BinaryObject> binaryCache = ignite.cache(cacheName).withKeepBinary();
        binaryCache.<Long, BinaryObject>withKeepBinary().invoke(
                key, new CacheEntryProcessor<Long, BinaryObject, Object>() {
                    public Object process(MutableEntry<Long, BinaryObject> entry,
                                          Object... objects) throws EntryProcessorException {
                        // Create builder from the old value.
                        BinaryObjectBuilder builder = entry.getValue().toBuilder();

                        //Update the field in the builder.
                        builder.setField(field, value);

                        // Set new value to the entry.
                        entry.setValue(builder.build());

                        return null;
                    }
                });
    }

    /**
     * Issues scan query based lookup to cache
     * @param ageThreshold query parameter
     * @return list of objects
     */
    public List<Person> getScanQueryResult(int ageThreshold) {
        try (QueryCursor<Cache.Entry<Long, Person>> cursor = igniteCache.query(new ScanQuery<Long, Person>((k, p) -> p.getAge() > ageThreshold))) {
            List<Person> persons = new ArrayList<>();
            for (Cache.Entry<Long, Person> entry: cursor) {
                persons.add(entry.getValue());
            }
            return persons;
        }
    }

    /**
     * Issues sql query based lookup to cache
     * @param name query parameter
     * @return list of objects
     */
    public List<Person> getSQLQueryResult(String name) {

        SqlQuery<Long, Person> sql = new SqlQuery<>(Person.class, "name = ?");

        // Find all persons earning more than 1,000.
        try (QueryCursor<Cache.Entry<Long, Person>> cursor = igniteCache.query(sql.setArgs(name))) {
            List<Person> persons = new ArrayList<>();
            for (Cache.Entry<Long, Person> e : cursor)
                persons.add(e.getValue());
            return persons;
        }
    }
}
