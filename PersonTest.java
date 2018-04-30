/**
 * Created by: Henry Huynh
 * ITEC 4260 Software Testing & QA
 * Assignment 6
 * Spring 2018
 */
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PersonTest {

    // Instance variables and objects
    private CacheManager cacheManager;
    private DiskManager diskManager;
    private int phoneNumber;
    private int phoneNumberOnCache;
    private int phoneNumberOnDisk;
    private Person personStoreToCache;
    private Person personStoreToDisk;

    /**
     * The setup() will initialize all instance variables and mock objects
     * This will be executed before anything else
     */
    @Before
    public void setup() {

        // Initializing all instances and mock objects
        cacheManager = mock(CacheManager.class);
        diskManager = mock(DiskManager.class);
        phoneNumber = 1234;
        phoneNumberOnCache = 5678;
        phoneNumberOnDisk = 4532;

        personStoreToCache = new Person(cacheManager, diskManager);
        personStoreToDisk = new Person(cacheManager, diskManager);
    }

    /**
     * The checkPersonNotOnDiskOrCache() will test if a person object is stored into the disk or cache
     */
    @Test
    public void checkPersonNotOnDiskOrCache() {

        // Checking condition to see if Disk and Cache calls getPerson()
        when(cacheManager.getPerson(phoneNumber)).thenReturn(null);
        when(diskManager.getPerson(phoneNumber)).thenReturn(null);

        // Running assertEquals method to check if both Disk and Cache memory return null from Disk and Cache
        Assert.assertEquals("Person does not exist in Cache", null, cacheManager.getPerson(phoneNumber));
        Assert.assertEquals("Person does not exist in Disk", null, diskManager.getPerson(phoneNumber));

        // Verifying that the getPerson() was called from Disk and Cache memory
        verify(cacheManager, times(1)).getPerson(phoneNumber);
        verify(diskManager, times(1)).getPerson(phoneNumber);
    }

    /**
     * The checkPersonInCache() will test if the person object is stored into Cache
     */
    @Test
    public void checkPersonInCache() {

        // Retrieving full name of Person stored in the Cache memory
        String fullName = personStoreToCache.getFullName();

        // Checking condition to see if person object was returned from Cache memory
        when(cacheManager.getPerson(phoneNumberOnCache)).thenReturn(personStoreToCache);

        // Running assertEquals method to check if person is stored on Disk
        Assert.assertEquals("Person object stored in Cache Memory", personStoreToCache, cacheManager.getPerson(phoneNumberOnCache));
        Assert.assertEquals(fullName, cacheManager.getPerson(phoneNumberOnCache).getFullName());

        // Verifying the getPerson() call was made twice
        verify(cacheManager, times(2)).getPerson(phoneNumberOnCache);
    }

    /**
     * The checkPersonInDisk() will test for person object stored on Disk but not in Cache
     */
    @Test
    public void checkPersonInDisk(){

        //Executing Mockito method to check if the getPerson() is called in Disk
        //and checking if it is not in Cache memory
        when(cacheManager.getPerson(phoneNumberOnDisk)).thenReturn(null);
        when(diskManager.getPerson(phoneNumberOnDisk)).thenReturn(personStoreToDisk);
        Person person = diskManager.getPerson(phoneNumberOnDisk);

        // Running assertEquals method to check if person is stored on Disk but not in Cache
        Assert.assertEquals("Person object should not be in Cache memory", null, cacheManager.getPerson(phoneNumberOnDisk));
        Assert.assertEquals("Person object should not be in Disk memory", personStoreToDisk, person);

        // Verifying the Person class did try to look up object stored in Cache memory
        verify(cacheManager, times(1)).getPerson(phoneNumberOnDisk);
    }
}
