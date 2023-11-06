import { DeleteOutline } from "@mui/icons-material";
import { InputBase, ListItem, ListItemText, Checkbox, ListItemSecondaryAction, IconButton } from "@mui/material";
import React, {useState} from "react";

const Todo = (props) => {
    const [item, setItem] = useState(props.item);
    const [readOnly, setReadOnly] = useState(true);

    const editItem = props.editItem;
    const deleteItem = props.deleteItem;


    // editEventHandler 작성
    const editEventHandler =(e)=>{
        setItem({...item, title: e.target.value});
    };

    // deleteEventHandler 작성
    const deleteEventHandler =()=>{
        deleteItem(item);
    };

    // checkboxEventHandler() 함수 구현
    const checkboxEventHandler=(e)=>{
        item.done=e.target.checked;
        editItem(item);
    }

    // turnOffReadOnly 함수 작성
    const turnOffReadOnly = ()=>{
        setReadOnly(false);
    }

    // turnOnReadOnly 함수 작성
    const turnOnReadOnly =(e)=>{
        if(e.key === "Enter" && readOnly === false){
            setReadOnly(true);
            editItem(item);
        }
    }

    return (
        <ListItem>
            <Checkbox checked={item.done} onChange={checkboxEventHandler}/>
            <ListItemText>
                <InputBase
                    inputProps={{ 
                        "aria-label": "naked",
                        readOnly: readOnly  }}
                    onClick={turnOffReadOnly}
                    onKeyDown={turnOnReadOnly}
                    onChange={editEventHandler}
                    type="text"
                    id={item.id}
                    name={item.id}
                    value={item.title}
                    multiline={true}
                    fullWidth={true}
                />
            </ListItemText>
            <ListItemSecondaryAction>
                <IconButton aria-label="Delete Todo"
                onClick={deleteEventHandler}>
                    <DeleteOutline />
                </IconButton>
            </ListItemSecondaryAction>
        </ListItem>
    );
};

export default Todo;