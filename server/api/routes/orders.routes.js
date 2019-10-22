const express =require('express');
const mongoose=require('mongoose');

const router=express.Router();

const Order=require('../models/orders.model');


router.post((req,res,next)=>{
    const ord=req.body;

    const order=new Order({
        _id:mongoose.Types.ObjectId(),
        menu_items:ord.menu_items,
        user:ord.user
    })

    order
    .save()
    .then(order=>{
        return res.status(200).json({
            message:"Order Saved",
            order
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Could Not Save Order"
        })
    })
})

module.exports=router;

