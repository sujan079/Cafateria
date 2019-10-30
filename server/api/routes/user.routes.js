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

    


router.get('/',(req,res,next)=>{
    User.find()
    .exec()
    .then(db_users=>{
        db_users.push({
            _id:"counter",
            email:'',
            active:true
        })
        return res.status(200).json({
            count:db_users.length,
            users:db_users
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Somthing went wrong"
        })
    })

})

router.post('/',(req,res,next)=>{
    const user=req.body;

    const newUser=new User({
        _id:user._id,
        email:user.email
    }); 

    User.findById(user._id)
    .exec()
    .then(user=>{
        if(user!=null){
            return res.status(200).json({
                message:"User exists"
            })
        }
        else{
                return newUser.save()
                .then((user)=>{
                    return res.status(200).json({
                        message:"User Successfully Added",
                        user
                    })
                })
                .catch(err=>{
                    console.log(err)
                    return res.status(500).json({
                        message:"Could Not Add User "
                    })
                }) ;
        }
        
    })
    
})

router.get('/:id',(req,res,next)=>{
    const id=req.params.id;

    User.findById(id)
    .exec()
    .then(user=>{
        if(user==null){
            return res.status(404).json({
                message:"No user found"
            })

        }else{
            return res.status(200).json({
                _id:user._id,
                email:user.email,
                active:user.active
            })
        }
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Invalid User ID"
        })
    })
})

router.patch('/:id',(req,res,next)=>{
    const id=req.params.id;
    const updateItem=req.body;
    User.findById(id)
    .exec()
    .then(user=>{
        if(user=null){
            return res.status(404).json({
                message:"User Not found"
            })
        }
        return User.updateOne({_id:id},{$set:{...updateItem}}).exec()
    })
    .then(updated_usr=>{
        return res.status(200).json({
            message:"user updated"
        })
    })
    .catch(err=>{
        return res.status(404).json({
            message:"Invalid user id"
        })
    })
})

router.delete('/:id',(req,res,next)=>{
    const id=req.params.id;

    User.deleteMany({_id:id})
    .exec()
    .then(()=>{
        return res.status(200).json({
            message:"delete Success"
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Could Not Delete User,No id"
        })
    })
})

module.exports=router;