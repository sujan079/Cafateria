const mongoose=require('mongoose');

const userSchema=mongoose.Schema({
    _id:String,
    firstName:{
        type:String,
        require:true
    },
    lastName:{
        type:String,
        require:true
    },
    phone_number:{
        type:Number,
        require:true
    },
    address:{
        type:String,
        require:true
    },
    profile:{
        type:String,
        require:true
    }


});

module.exports = mongoose.model('user',userSchema);