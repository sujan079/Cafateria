const express=require('express');
const mongoose=require('mongoose');
const router=express.Router();

const MenuItem=require('../models/menu_item.model');
const MenuItemCategories=require('../models/menu_item_categories.model');

//displays all menu-items
router.get("/",(req,res,next)=>{
    MenuItem
    .find()
    .sort({'itemName':-1})
    .exec()
    .then(menuItems=>{
        return res.status(200).json({
            count:menuItems.length,
            menuItems:[...menuItems]
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Somthing went wrong Could not get data"
        })
    })
})

//add new Menu Item
router.post("/",(req,res,next)=>{
    const item=req.body

    console.log(item)
    const newItem=new MenuItem({
        _id:mongoose.Types.ObjectId(),
        itemName:item.itemName,
        price:item.price,
        categories:item.categories
    })

    newItem.categories.forEach(category => {
        MenuItemCategories.find({category:category})
        .exec()
        .then((result)=>{
            console.log(result)
            if(result.length<=0){
                const menuItemCategory=new MenuItemCategories({
                    _id:mongoose.Types.ObjectId(),
                    category:category
                })
                menuItemCategory
                .save()
            }
        })
        .catch(err=>{
            console.log(err)
        });
    })

    
    if(item.itemName==''|| item.price<0 || item.categories.length<=0){
        return res.status(500).json({
            message:"Invalid Field Data"
        })
    }

    else{
        newItem
        .save()
        .then((addedItem)=>{
            return res.status(200).json({
                message:"ADD Success",
                addedItem
            })
        })
        .catch(err=>{
            console.log(err)
            return res.status(500).json({
                message:"Error Could Not add Item"
            })
        })
    }

})

//get menu item by id
router.get("/:id",(req,res,next)=>{
    const id=req.params.id;
    MenuItem.findById(id)
    .exec()
    .then(item=>{
        console.log(item)
        if(item==null){
            return res.status(500).json({
                message:"No Item with id:"+id
            })
        }
        return res.status(200).json({
            item
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"IN-Valid Item id:"+id
        })
    })
})

router.patch("/:id",(req,res,next)=>{
    const id=req.params.id
    const updatedItem=req.body
    console.log(updatedItem)
    MenuItem.update({_id:id},{$set:{...updatedItem}})
    .exec()
    .then((item)=>{
        if(item==null){
            return res.status(500).json({
                message:"No Item with id"+id
            })
        }
        return res.status(200).json({
            message:"Update Successful",
        })
    })
    .catch(err=>{
        console.log(err)

        return res.status(500).json({
            
            message:"Could not update item id:"+id
        })
    })
})

//delete menu item
router.delete("/:id",(req,res,next)=>{
    const id=req.params.id
    MenuItem.deleteOne({_id:id})
    .exec()
    .then(()=>{
        return res.status(200).json({
            message:"Deleted Item id:"+id
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Could Not Delete item with id:"+id
        })
    })
})

module.exports =router;