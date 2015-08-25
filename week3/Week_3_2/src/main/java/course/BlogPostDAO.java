package course;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlogPostDAO {
    private static final String COMMENTS_KEY = "comments";
	MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        Document post = postsCollection //
        		.find(eq("permalink", permalink)) //
        		.first();

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // Return a list of DBObjects, each one a post from the posts collection
        List<Document> posts = postsCollection //
        		.find() //
        		.sort(descending("date")) //
        		.limit(limit) //
        		.into(new ArrayList<Document>());

        return posts;
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        Document post = new Document();
        post.put("author", username);
        post.put("title", title);
        post.put("body", body);
        post.put("permalink", permalink);
        post.put("tags", tags);
        post.put(COMMENTS_KEY, new ArrayList<Document>());
        post.put("date", new Date());
        this.postsCollection.insertOne(post);
        
        return permalink;
    }




    // White space to protect the innocent








    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body, final String permalink) {

    	// Hints:
    	// - email is optional and may come in NULL. Check for that.
    	// - best solution uses an update command to the database and a suitable
    	//   operator to append the comment on to any existing list of comments

    	Document comment = new Document();
    	comment.put("author", name);
    	comment.put("body", body);
    	if(email != null && !"".equals(email))
    		comment.put("email", email);
    	
    	Document post = this.findByPermalink(permalink);
    	
    	this.postsCollection.updateOne(post, new BasicDBObject("$push", new BasicDBObject("comments", comment)));
    }
}
