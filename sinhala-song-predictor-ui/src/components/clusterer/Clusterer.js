import React from "react";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select} from "@mui/material";
import Box from "@mui/material/Box";
import SongUploader from "../common/SongUploader";

const Clusterer = () => {
    const [selectedAlgorithm, setSelectedAlgorithm] = React.useState("K-means")
    const handleClusterAlgoChange = (event) => {
        setSelectedAlgorithm(event.target.value)
    }

    function executeClusterer() {

    }

    return (
        <Box id="clusterer" className="clusterer">
            <br/><br/><br/>
            <Grid container>
                <Grid item xs={12} sm={6} md={3}>
                    <FormControl fullWidth sx={{paddingRight: '40px'}}>
                        <InputLabel id="cluster-lbl">Choose Clusterer Algorithm</InputLabel>
                        <Select
                            labelId="cluster-lbl"
                            id="cluster-select"
                            label="Choose Clusterer Algorithm"
                            value={selectedAlgorithm}
                            onChange={handleClusterAlgoChange}
                        >
                            <MenuItem value="K-means">K-means</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
            </Grid>
            <br/>
            <Grid id={'song-uploader'} container  justifyContent="center">
                <Grid item xs={12} sm={6} md={3}>
                    <SongUploader/>
                </Grid>
            </Grid>
                <Grid container justifyContent="flex-end">
                    <Button id="clusterer-button" size="medium" variant="contained" color='secondary'
                            style={{marginTop: "20px"}} onClick={() => executeClusterer()}>SHOW CLUSTER</Button>
            </Grid>
            <Grid container justifyContent="center">
                <h1>Result is</h1>
            </Grid>
        </Box>
    )

}

export default Clusterer;