# BooksController

All URIs are relative to `""`

The controller class is defined in **[BooksController.java](../../src/main/java/example/micronaut/library/controller/BooksController.java)**

Method | HTTP request | Description
------------- | ------------- | -------------
[**addBook**](#addBook) | **POST** /add | Add a new book
[**search**](#search) | **GET** /search | Search for a book

<a name="addBook"></a>
# **addBook**
```java
Mono<Object> BooksController.addBook(bookInfo)
```

Add a new book

### Parameters
Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
**bookInfo** | [**BookInfo**](../../docs/models/BookInfo.md) |  |



### HTTP request headers
 - **Accepts Content-Type**: `application/json`
 - **Produces Content-Type**: Not defined

<a name="search"></a>
# **search**
```java
Mono<List<BookInfo>> BooksController.search(bookNameauthorName)
```

Search for a book

### Parameters
Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
**bookName** | `String` |  | [optional parameter]
**authorName** | `String` |  | [optional parameter]

### Return type
[**List&lt;BookInfo&gt;**](../../docs/models/BookInfo.md)


### HTTP request headers
 - **Accepts Content-Type**: Not defined
 - **Produces Content-Type**: `applicaton/json`

