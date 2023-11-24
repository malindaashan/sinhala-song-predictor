import * as React from 'react';
import {useState} from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import {CssBaseline, Drawer, List, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import {MENU_ITEMS} from "../../common/Constants";
import MicrowaveIcon from '@mui/icons-material/Microwave';
import AcUnitIcon from '@mui/icons-material/AcUnit';
import Box from "@mui/material/Box";
import MLPredictor from "../mlpredictor/MLPredictor";
import NLP from "../nlp/NLP";
import TranslateIcon from '@mui/icons-material/Translate';
import Hybrid from "../hybrid/Hybrid";

const HomeDrawer = () => {
    const [drawerWidth, setDrawerWidth] = useState(230);
    const [activePage, setActivePage] = useState("ML-PREDICTOR");
    const [menuSelection, setMenuSelection] = useState(0);

    const handleMenuBarClick = (event, text, index) => {
        setActivePage(text);
        setMenuSelection(index)
    }
    return (
        <>
            <Box sx={{display: 'flex', width: '100%', height: '100vh', background: '#EEEEEE'}}>
                <CssBaseline/>
                <AppBar position="fixed" sx={[{background: '#8f4f9d'},
                    {zIndex: (theme) => theme.zIndex.drawer + 1}]}>
                    <Toolbar>
                        <Typography variant="h6" component="div"
                                    sx={{flexGrow: 1, alignItems: 'left', paddingLeft: "230px"}}>
                            SINHALA MUSIC PREDICTOR
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Drawer
                    variant="permanent"
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        '& .MuiDrawer-paper': {
                            width: drawerWidth,
                            boxSizing: 'border-box',
                        },
                    }}
                    open={true}
                >
                    <Toolbar/>
                    <Box sx={{overflow: 'disable'}}>
                        <List>
                            {MENU_ITEMS.split(",").map((text, index) => (
                                <ListItem key={text} onClick={(e) => handleMenuBarClick(e, text, index)}
                                          selected={index === menuSelection}
                                          disablePadding>
                                    <ListItemButton>
                                        <ListItemIcon>
                                            {index === 0 ? <MicrowaveIcon/> : index === 1 ? <TranslateIcon/> :
                                                <AcUnitIcon/>}
                                        </ListItemIcon>
                                        <ListItemText primary={text}/>
                                    </ListItemButton>
                                </ListItem>
                            ))}
                        </List>
                    </Box>
                </Drawer>
                <Box component="main" sx={{flexGrow: 1, p: 3, background: '#ffffff'}}>
                    <Toolbar/>
                    <br/><br/>
                    {activePage === "ML-PREDICTOR" ? <MLPredictor/> : activePage === "NLP-PREDICTOR" ? <NLP/> :
                        <Hybrid/>}
                </Box>
            </Box>
        </>
    )

}

export default HomeDrawer;