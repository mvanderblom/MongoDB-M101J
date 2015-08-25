/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.mvdb.mongodb;

import static com.mongodb.client.model.Sorts.ascending;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class RemoveStudentsLowestScore {
	public static void main(String[] args) {
		MongoClient client = new MongoClient();
		try {
			MongoDatabase database = client.getDatabase("students");
			MongoCollection<Document> collection = database.getCollection("grades");
			
			MongoCursor<Document> iterator = collection.find()
					.sort(ascending("student_id", "score"))
					.iterator();
			
			Integer lastStudentId = null;
			while(iterator.hasNext())
			{
				Document gradingDoc = iterator.next();
				Integer studentId = gradingDoc.getInteger("student_id");
				if(!studentId.equals(lastStudentId))
				{
					lastStudentId = studentId;
					System.out.println(lastStudentId);
					collection.deleteOne(gradingDoc);
				}
			}
			
			System.out.println(collection.count());
		} finally {
			client.close();
		}
	}
}
