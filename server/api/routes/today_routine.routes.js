const express=require('express');

const router=express.Router();

const MenuItems=require('../models/menu_item.model');
const MenuItemCategories=require('../models/menu_item_categories.model');

router.get('/',(req,res,next)=>{
   let categories=[];
   const categoryType=req.query.category

    MenuItemCategories.find()
    .exec()
    .then(result=>{
        categories=result;
        if(categoryType==="ALL"){
            return MenuItems.find().exec()
        }else{
            return MenuItems.find({categories:categoryType}).exec()
        }
    })
    .then(menu_items=>{
        res.status(200).json({
            categories:categories.map(category=>{
                return category.category
            }),
            menu_items:menu_items.map(item=>{
                return {
                    itemName:item.itemName,
                    price:item.price,
                    categories:item.categories
                }
            })
        })
    })
    .catch(err=>{
        console.log(err)
    })

});

module.exports = router;