const express =require('express');
const mongoose=require('mongoose');

const router=express.Router();

const OrderHistory=require('../models/order_history.model');
const Order=require('../models/orders.model');
const User=require('../models/user.model');


router.post('/',(req,res,next)=>{
    const ord=req.body;

    console.log(ord)

    if(!ord.user || ord.menu_items.length<=0){
       return res.status(500).json({
            message:"Cannot Place Empty Order or order with no user id"
        })
    }
    User
    .findById(ord.user)
    .exec()
    .then(user=>{
        if(user==null){
            return res.status(404).json({
                message:"No user with such id"
            })
        }else{
            if(!user.active){
                //302 is a error for not active user
                return res.status(302).json({
                    message:"Account Not active"
                })
            }
            var today = new Date();
            var dd = String(today.getDate()).padStart(2, '0');
            var mm = String(today.getMonth() + 1).padStart(2, '0');
            var yyyy = today.getFullYear();
            
            todayDate = yyyy + '-' + mm + '-' + dd;     

            ord.menu_items.forEach(order => {
                const orderHistory=new OrderHistory({
                    _id:mongoose.Types.ObjectId(),
                    itemName:order.itemName,
                    price:order.price,
                    order_by:ord.user,
                    categories:order.categories,
                    quantity:order.quantity,
                    order_date:todayDate,
                    order_time:today.getHours()+":"+today.getMinutes()+":"+today.getSeconds(),
                    date:new Date().getTime()
                });
                
            

                orderHistory.save()
                .then(()=>{
                    console.log("order saved")

                })
                .catch(err=>{
                    console.log(err)
                })
            });

            res.status(200).json({
                message:"Order Registered"
            })
        }
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Invalid user id"
        })
    })

})

module.exports=router;

