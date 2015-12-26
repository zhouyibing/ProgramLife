package framework.guava.utilities;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

public class Book implements Comparable<Book> {
private Person author;
private String title;
private String publisher;
private String isbn;
private double price;

public Person getAuthor() {
	return author;
}

public void setAuthor(Person author) {
	this.author = author;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getPublisher() {
	return publisher;
}

public void setPublisher(String publisher) {
	this.publisher = publisher;
}

public String getIsbn() {
	return isbn;
}

public void setIsbn(String isbn) {
	this.isbn = isbn;
}

public double getPrice() {
	return price;
}

public void setPrice(double price) {
	this.price = price;
}

public String toString() {
return Objects.toStringHelper(this)
.omitNullValues()
.add("title", title)
.add("author", author)
.add("publisher", publisher)
.add("price",price)
.add("isbn", isbn).toString();
}

@Override
public int compareTo(Book o) {
	return ComparisonChain.start()
			.compare(this.title, o.getTitle())
			.compare(this.author, o.getAuthor())
			.compare(this.publisher, o.getPublisher())
			.compare(this.isbn, o.getIsbn())
			.compare(this.price, o.getPrice())
			.result();
}

@Override
public int hashCode() {
	return Objects.hashCode(title, author, publisher, isbn);//会对传入的字段序列计算出合理的、顺序敏感的散列值。
}


 class Person implements Comparable<Person>{
	 String name;
	 String job;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	 
	public String toString() {
		return Objects.toStringHelper(this).omitNullValues().add("name", name).add("job", job).toString();
	}
	@Override
	public int compareTo(Person o) {
		return ComparisonChain.start()
				.compare(this.job, o.getJob())
				.compare(this.name, o.getName())
				.result();
	}
 }
 
 public static void main(String[] args) {
	Book book = new Book();
	book.setIsbn("sfsdf");
	book.setPrice(123.4);
	book.setPublisher("zzz");
	book.setTitle("bbbbbbbbbb");
	Person p =  book.new Person();
	p.setJob("it");
	p.setName("zhou");
	book.setAuthor(p);
	System.out.println(book);
	System.out.println(Objects.firstNonNull("ss","sddd"));
	System.out.println("book's hashcode:"+book.hashCode());
}
}