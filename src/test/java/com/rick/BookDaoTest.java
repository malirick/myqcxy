//package com.rick;
//
//
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class BookDaoTest {
//       @Autowired
//      private BookDao bookDao;
//       @Autowired
//       private BookService bookService;
//
//   @Test
//    public void addTest(){
//       LambdaQueryWrapper<Book> lqw=new LambdaQueryWrapper<Book>();
//       lqw.like(Book::getName,"红");
//
//       Page<Book> page=new Page(1,2);
//       bookDao.selectPage(page,lqw);
//
//   }
//
//    @Test
//    public void idTest(){
//        System.out.println(bookDao.selectById(1));
//    }
//
//
//    @Test
//    public void testsave(){
//      Book book=new Book();
//      book.setName("三体2");
//      book.setType("科幻");
//      bookService.save(book);
//    }
//
//
//
//}
