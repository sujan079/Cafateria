const mongoose=require('mongoose');

const userSchema=mongoose.Schema({
    _id:String,
    email:{
        type:String,
        require:true
    },

    displayName:{
        type:String,
        require:true
    },

    phone_number:{
        type:String,
    },
    address:{
        type:String
    },
    profile_img:{
        type:String
    }


});

module.exports = mongoose.model('user',userSchema);