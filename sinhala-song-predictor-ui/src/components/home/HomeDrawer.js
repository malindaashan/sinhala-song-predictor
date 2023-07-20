import * as React from 'react';
import {useState} from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import {Divider, Drawer, Grid, List, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import {MENU_ITEMS} from "../../common/Constants";
import MicrowaveIcon from '@mui/icons-material/Microwave';
import AcUnitIcon from '@mui/icons-material/AcUnit';
import BlurOnIcon from '@mui/icons-material/BlurOn';
import Box from "@mui/material/Box";
import MLPredictor from "../mlpredictor/MLPredictor";
import DLPredictor from "../dlpredictor/DLPredictor";
import Clusterer from "../clusterer/Clusterer";

const HomeDrawer = () => {
    const [drawerWidth, setDrawerWidth] = useState(230);
    const [activePage, setActivePage] = useState("ML-PREDICTOR");

    const handleMenuBarClick = (event, text, index) => {
        setActivePage(text);
    }
    return (
        <>
            <Grid container columnSpacing={{xs: 1, sm: 1, md: 2}} id="home-drawer" className="home-drawer">
                <Grid item xs={12} sm={12} md={12}>
                    <AppBar position="fixed" sx={{background: '#8f4f9d'}}>
                        <Toolbar>
                            <Typography variant="h6" component="div" sx={{flexGrow: 1, alignItems: 'left'}}>
                                SINHALA MUSIC PREDICTOR - DL
                            </Typography>
                        </Toolbar>
                    </AppBar>
                </Grid>
                <Drawer
                    sx={{
                        width: drawerWidth,
                        flexShrink: 0,
                        '& .MuiDrawer-paper': {
                            width: drawerWidth,
                            boxSizing: 'border-box',
                        },
                    }}
                    variant="permanent"
                    anchor="left"
                >
                    <Toolbar/>
                    <Divider/>
                    <List>
                        {MENU_ITEMS.split(",").map((text, index) => (
                            <ListItem key={text} onClick={(e) => handleMenuBarClick(e, text, index)} disablePadding>
                                <ListItemButton>
                                    <ListItemIcon>
                                        {index === 0 ? <MicrowaveIcon/> : index === 1 ? <BlurOnIcon/> : <AcUnitIcon/>}
                                    </ListItemIcon>
                                    <ListItemText primary={text}/>
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Drawer>
                <Box component="main" sx={{flexGrow: 1, p: 3, background: '#ffffff'}}>
                 <br/><br/>
                    {activePage === "ML-PREDICTOR" ? <MLPredictor/> : activePage === "DL-PREDICTOR" ? <DLPredictor/> :
                        <Clusterer/>}
                </Box>


            </Grid>
        </>
    )

}

export default HomeDrawer;