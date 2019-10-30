const mongoose=require('mongoose');

const routineSchema=mongoose.Schema({
    _id:mongoose.Schema.Types.ObjectId,
    itemName:{
        type:String,
        require:true
    },
    price:{
        type:Number,
        require:true
    },
    categories:{
        type:[String],
        require:true
    },
    day:{
        type:String,
        require:true
    }
});

module.exports = mongoose.model('routine-item',routineSchema);