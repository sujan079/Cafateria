const express=require('express');
const mongoose=require('mongoose');
const router=express.Router();

const MenuItem=require('../models/menu_item.model');

//displays all menu-items
router.get("/",(req,res,next)=>{
    MenuItem
    .find()
    .exec()
    .then(menuItems=>{
        res.status(200).json({
            count:menuItems.length,
            menuItems:[...menuItems]
        })
    })
    .catch(err=>{
        res.status(500).json({
            message:"Somthing went wrong Could not get data"
        })
    })
})

//add new Menu Item
router.post("/",(req,res,next)=>{
    const item=req.body
    const newItem=new MenuItem({
        _id:mongoose.Types.ObjectId,
        ...item
    })



    newItem
    .save()
    .then()
    .catch(err=>{
        res.status()
    })

    console.log(menuItem)
})

//get menu item by id
router.get("/:id",(req,res,next)=>{

})

router.patch("/:id",(req,res,next)=>{

})

//delete menu item
router.delete("/:id",(req,res,next)=>{

})

module.exports =router;