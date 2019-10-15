const express=require('express');

const router=express.Router();

const MenuItems=require('../models/menu_item.model');
const MenuItemCategories=require('../models/menu_item_categories.model');

router.get('/',(req,res,next)=>{
   let categories=[];
    MenuItemCategories.find()
    .exec()
    .then(result=>{
        console.log(result)
        categories=result;
        return MenuItems.find().exec()
    })
    .then(menu_items=>{
        res.status(200).json({
            categories:categories,
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