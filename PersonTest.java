/**
 *  ITEC 4260
 *  Dr. Im
 *
 *  Assignment 6: Utilizing Mockito library to construct mock objects and test it's behaviors
 *
 *  Created by Henry Huynh
 *  Georgia Gwinnett College

 *  Date: 4/09/2018
 */
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PersonTest {

    CacheManager cacheManager = mock(CacheManager.class);
    DiskManager diskManager = mock(DiskManager.class);
    int phoneNumber = 1234;
    int phoneNumberOnCache = 5678;
    int phoneNumberOnDisk = 4532;
    Person personStoreToCache = new Person(cacheManager, diskManager);
    Person personStoreToDisk = new Person(cacheManager, diskManager);

    // Testing for person not in the Disk nor the Cache
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

    // Testing for person in cache
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


    // Testing for person stored on Disk but not in Cache
    @Test
    public void checkPersonInDisk(){

        /* Executing Mockito method to check if the getPerson() is called in Disk
           and checking if it is not in Cache memory
         */
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
