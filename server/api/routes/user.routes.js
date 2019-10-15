const express=require('express');
const mongoose=require('mongoose');
const multer=require('multer');

const router=express.Router();

const User=require('../models/user.model');

const storage=multer.diskStorage({
    destination:function(req,file,cb){
        cb(null,'./uploads');
    },
    filename:function(req,file,cb){
        cb(null,new Date().toISOString+file.filename);
    }
});

const fileFilter=(req,file,cb)=>{
    if(file.mimetype==='image/jpeg'||file.mimetype==='image/png'){
        cb(null,true)
    }else{
        cb(new Error("File not Image"),false);
    }
}
const upload=multer(
    {
        storage:storage,
        limits:1024*1024*10,
        fileFilter:fileFilter
    }
    
    );

router.post('/',upload.single(),(req,res,next)=>{
    const user=req.body;

    const newUser=new User({
        _id:user.phone_number,
        firstName:user.firstName,
        lastName:user.lastName,
        phone_number:user.phone_number,
        address:user.address,
        profile:req.file.path
    }); 

    newUser
    .save()
    .then((user)=>{
        return res.status().json({
            message:"User Successfully Added",
            user
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Could Not Add User "
        })
    }) 
})