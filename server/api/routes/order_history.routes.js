const express=require('express');
const mongoose=require('mongoose');

const router=express.Router();

const OrderHistory=require('../models/order_history.model');

// TODO:Some Check For Valid User request you dont want to show every one informations


//get all order history
router.get('/',(req,res,next)=>{

});

//add new order history
router.post('/',(req,res,next)=>{
    const order=req.body;

    const orderHistory=new OrderHistory({
        _id:mongoose.Types.ObjectId(),
        itemName:order.itemName,
        price:order.price,
        order_by:order.order_by||null,
        categories:order.categories,
        quantity:order.quantity,
        order_date:order.order_date,
        order_time:order.order_time
    });

    orderHistory
    .save()
    .then((orderHistory)=>{
        return res.status(200).json({
            message:"Success Added Order History",
            ...orderHistory
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Could not add Order History"+err
        })
    })


})

//get an individual order history
router.get('/:id',(req,res,next)=>{
    const id=req.params.id;
    OrderHistory.findById(id)
    .exec()
    .then(orderHistory=>{
        if(orderHistory==null){
            return res.status(404).json({
                message:"No Such Order with ID:"+id
            })
            res.status(200).json({
                ...orderHistory
            })
        }
    })
    .catch(err=>{
        return res.status(500).json({
            message:"InValid order History ID:"+id
        })
    })
})

router.delete('/:id',(req,res,next)=>{
    const id=req.params.id;

    OrderHistory.findByIdAndDelete(id)
    .exec()
    .then(()=>{
        return res.status(200).json({
            message:"Delete Successful"
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Could not delete order history ID:"+id
        })
    })

})



module.exports=router;
