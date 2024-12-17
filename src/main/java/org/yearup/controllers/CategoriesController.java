package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;


    // create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // add the appropriate annotation for a get action
    @RequestMapping(path = "/categories", method = RequestMethod.GET)

    public List<Category> getAll()
    {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @RequestMapping(path="/categories/{id}", method = RequestMethod.GET)

    public Category getById(@PathVariable (name="id") int id, HttpServletResponse response)
    {
        // get the category by id
        Category categoryId = categoryDao.getById(id);
        if (categoryId == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return categoryId;
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    @RequestMapping(path="/categories/{categoryId}/products", method = RequestMethod.GET)

    public List<Product> getProductsById(@PathVariable(name="categoryId") int categoryId, HttpServletResponse response)
    {
        // get a list of product by categoryId
        List<Product> products= productDao.listByCategoryId(categoryId);

        if (products == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return products;
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/categories", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category)
    {
        // insert the category
        System.out.println("Incoming category: " + category);
        Category category1 = categoryDao.create(category);
        System.out.println("Returned category: " + category1);
        return  category1;
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(path = "/categories/{id}", method = RequestMethod.PUT)
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
        categoryDao.update(id, category);

    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
    }
}
