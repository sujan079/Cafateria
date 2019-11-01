const mongoose=require('mongoose');

const userSchema=mongoose.Schema({
    _id:String,
    email:{
        type:String,
        require:true
    },
    active:{
        type:Boolean,
        default:false
    }


});

module.exports = mongoose.model('user',userSchema);