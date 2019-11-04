const express=require('express');
const mongoose=require('mongoose');

const router=express.Router();

const MenuItems=require('../models/menu_item.model');
const RoutineItem=require('../models/routine.model');
const MenuItemCategories=require('../models/menu_item_categories.model');

router.get('/',(req,res,next)=>{

    let categories=[];
    let categoryType=req.query.category;
    const selectedDay=req.query.day;

    if(categoryType){
        categoryType=categoryType.trim()
    }

    
    let day='';

    if(selectedDay){
        day=selectedDay
    }else{
        switch(new Date().getDay()){
            case 0:
                day="SUNDAY"
                break;
            case 1:
                day="MONDAY"
                break;
            case 2:
                day="TUESDAY"
                break;
            case 3:
                day="WEDNESDAY";
                break;
            case 4:
                day="THURSDAY";
                break;
            case 5:
                day="FRIDAY";
                break;
            case 6:
                day="SATURDAY";
                break;    
        }
    }


    console.log(day)
     MenuItemCategories.find()
     .exec()
     .then(result=>{
         categories=result;
             return RoutineItem.find({$and:
                [
                    (categoryType)?{categories:categoryType}:{},
                    (day)?{day:day}:{}
                ]}).exec()

     })
     .then(menu_items=>{
         res.status(200).json({
             categories:categories.map(category=>{
                 return category.category
             }),
             routineItems:menu_items.map(item=>{
                 return {
                     _id:item._id,
                     itemName:item.itemName,
                     price:item.price,
                     categories:item.categories,
                     day:item.day
                 }
             })
         })
     })
     .catch(err=>{
         console.log(err)
     })
 
 });

// router.post('/',(req,res,next)=>{
//     const menuItemId=req.body.menu_item;
//     const routineItems=req.body;

//     MenuItems.find({_id:menuItemId})
//     .exec()
//     .then(db_menuItem=>{
//         if(db_menuItem==null){
//             return res.status(404).json({
//                 message:"Menu Item Not found"
//             })
//         }

//         return new RoutineItem({
//             _id:mongoose.Types.ObjectId(),
//             menu_item:routineItem.menu_item,
//             day:routineItem.day
//         }).save()

//     })
//     .then(db_routine_item=>{
//         return res.status(200).json({
//             message:"Item Added To Routine",
//             db_routine_item
//         })
//     })
//     .catch(err=>{
//         
//     })
// })

router.post('/',(req,res,next)=>{
    const routineItems=req.body.routineItems;
    const day=req.query.day;

    console.log(routineItems)


    RoutineItem.deleteMany({day:day}).exec()
    .then(()=>{

        routineItems.forEach(item => {
            new RoutineItem({
                _id:mongoose.Types.ObjectId(),
                itemName:item.itemName,
                price:item.price,
                categories:item.categories,
                day:item.day
            }).save()
        });
    
        return res.status(200).json({
            message:"Routine Saved"
        })

    })

})

router.delete('/',(req,res,next)=>{
    const id=req.body.menu_item;
    const day=req.body.day;


    RoutineItem.deleteMany({$and:[
        {_id:id},{day:day}
    ]})
    .exec()
    .then(()=>{
        return res.status(200).json({
            message:"delete Succesfull"
        })
    })
    .catch(err=>{
        return res.status(500).json({
            message:"Invalid Id"
        })
    })
})

router.delete('/:id',(req,res,next)=>{
        RoutineItem.deleteMany({_id:req.params.id}).exec()
        .then(()=>{
            return res.status(200).json({
                message:"Deleted"
            })
        })
        .catch(err=>{
            res.status(500).json({
                message:"Invalid Id"
            })
        })
})




module.exports = router;