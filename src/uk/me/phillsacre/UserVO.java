package uk.me.phillsacre;


/**
 * Interface to define a User value object.
 * 
 * @author psacre
 *
 */
public interface UserVO extends Comparable<UserVO>
{
	/**
	 * Retrieves this user's ID
	 * 
	 * @return
	 */
	public Integer getId();
	
	/**
	 * Retrieve this user's name
	 * 
	 * @return
	 */
	public String getName();
}
