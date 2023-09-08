import React from "react";
import Box from "@mui/material/Box";
import Loader from "../common/Loader";
import ClustererService from "../../service/ClustererService";
import {Button, Checkbox, FormControlLabel, FormGroup, Grid} from "@mui/material";
import SongUploader from "../common/SongUploader";
import DataSetCreatorService from "../../service/DataSetCreatorService";

const DataSetCreator = () => {
    const [loading, setLoading] = React.useState(false);
    const [selectedFile, setSelectedFile] = React.useState();
    const [algorithmsSelected, setAlgorithmsSelected] = React.useState([]);

    const handleChange = (event) => {
        setAlgorithmsSelected({
            ...algorithmsSelected,
            [event.target.name]: event.target.checked,
        });
    };

    function extractSaveFeatures() {
        setLoading(true);
        DataSetCreatorService.saveAndExtractFeatures(algorithmsSelected, selectedFile)
            .then((response) => {
                if (response.success) {
                    console.log(response.data);
                    setLoading(false);
                } else {
                    alert("Failed");
                    setLoading(false);
                }
            })
            .catch((ex) => {
                console.log("error");
                setLoading(false);
            });
    }

    return (
        <>
            {loading ? <Loader/> : null}
            <Box id="dataset-creator" className="dataset-creator">
                <h1> DataSet Creator</h1>
                <h6>The inputs audio files would be saved and used for training deep learning algorithm for sinhala
                    music
                    classfication</h6>
                <br/>
                <Grid id={'song-uploader'} container justifyContent="center">
                    <Grid item xs={12} sm={6} md={3}>
                        <SongUploader
                            setSelectedFile={setSelectedFile}
                        />
                    </Grid>
                </Grid>

                <FormGroup>
                    <FormControlLabel control={<Checkbox name={"SMO"} onClick={(e) => handleChange(e)}/>} label="SMO" />
                    <FormControlLabel control={<Checkbox  name={"Naive Bayes"} onClick={(e) => handleChange(e)}/>} label="NAIVE BAYES" />
                    <FormControlLabel control={<Checkbox name={"Random Forest"} onClick={(e) => handleChange(e)}/>} label="RANDOM FOREST" />

                    <FormControlLabel control={<Checkbox name={"K-Means"} onClick={(e) => handleChange(e)}/>} label="K MEANS" />
                    <FormControlLabel control={<Checkbox name={"Hierarchical-Clustering"} onClick={(e) => handleChange(e)}/>} label="HIERARCHICAL CLUSTERING" />
                </FormGroup>

                <Grid container justifyContent="flex-end">
                    <Button id="dataset-creator-button" size="medium" variant="contained" color='secondary'
                            style={{marginTop: "20px"}} onClick={() => extractSaveFeatures()}>EXTRACT PREDICT FEATURES</Button>
                </Grid>

            </Box>
        </>
    )
}

export default DataSetCreator;
