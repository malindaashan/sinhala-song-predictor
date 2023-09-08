import React from "react";
import Box from "@mui/material/Box";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select} from "@mui/material";
import SongUploader from "../common/SongUploader";
import MLPredictorService from "../../service/MLPredictorService";
import ResultGrid from "../common/ResultGrid";
import Loader from "../common/Loader";

const MLPredictor = () => {
    const [selectedAlgorithm, setSelectedAlgorithm] = React.useState("Random-Forest")
    const [selectedFile, setSelectedFile] = React.useState();
    const [predictionResponse, setPredictionResponse] = React.useState(null);
    const [loading, setLoading] = React.useState(false);
    const handleClassifierAlgoChange = (event) => {
        setSelectedAlgorithm(event.target.value)
        setPredictionResponse(null);
    }

    function executeClassifier() {
        setLoading(true);
        MLPredictorService.executeClassifier(selectedAlgorithm, selectedFile)
            .then((response) => {
                if (response.success) {
                    setPredictionResponse(response.data);
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
            <Box id="ml-predictor" className="ml-box">
                <h1 justifyContent="center"> ML-Classifier</h1>
                <br/><br/><br/>
                <Grid container>
                    <Grid item xs={12} sm={6} md={3}>
                        <FormControl fullWidth sx={{paddingRight: '40px'}}>
                            <InputLabel id="cluster-lbl">Choose Classifier Algorithm</InputLabel>
                            <Select
                                labelId="classifier-lbl"
                                id="classifier-select"
                                label="Choose Classifier Algorithm"
                                value={selectedAlgorithm}
                                onChange={handleClassifierAlgoChange}
                            >
                                <MenuItem value="SMO">SMO</MenuItem>
                                <MenuItem value="Naive-Bayes">Naive-Bayes</MenuItem>
                                <MenuItem value="Random-Forest">Random-Forest</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                </Grid>
                <br/>
                <Grid id={'song-uploader'} container justifyContent="center">
                    <Grid item xs={12} sm={6} md={3}>
                        <SongUploader
                            setSelectedFile={setSelectedFile}
                        />
                    </Grid>
                </Grid>
                <Grid container justifyContent="flex-end">
                    <Button id="clusterer-button" size="medium" variant="contained" color='secondary'
                            style={{marginTop: "20px"}} onClick={() => executeClassifier()}>CLASSIFY</Button>
                </Grid>
                <ResultGrid predictionResponse={predictionResponse}/>
            </Box>
        </>
    )

}

export default MLPredictor;